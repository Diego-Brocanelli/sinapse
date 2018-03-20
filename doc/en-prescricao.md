# Memed Sinapse Prescrição (Memed Synapse Prescription)

Synapse Prescription is Memed's 100% free tool which allows your site/platform to have a smart prescription with several functionalities:

- Protocols - Practicity and speed prescribing a group of drugs.
- My Dosage - Agility for the doctor to prescribe based on their history.
- Smart printing - Print rules, margin control, custom logo and special prescriptions printing.
- Send by SMS - Agility for the patient, allowing prices comparison.
- Drugs database - The most complete in Brazil, with information constantly curated by our team.
- Medicamentos - Drugs
- Posologia - Posology

## Dictionary

- CRM - Is the Brazilian Health Professional individual registry identification (ex: 123456SP)
- CPF - The Cadastro de Pessoas Físicas (CPF; Portuguese for "Natural Persons Register") is the Brazilian individual taxpayer registry identification, a number attributed by the Brazilian Federal Revenue to both Brazilians and resident aliens who pay taxes or take part, directly or indirectly, in activities that provide revenue for any of the dozens of different types of taxes existing in Brazil.
- UF - The Federative Republic of Brazil is a union of 27 Federative Units (Portuguese: Unidades Federativas, UF) (ex: SP, RJ, MG)
- Medical Specialty - A specialty, or speciality, in medicine is a branch of medical practice (ex: Dermatologista [Dermatologist])

## How to integrate
The integration is separated in two steps: implementing the user registration (health professional with CRM) via API and then implementing Synapse Prescription (front-end) on your system.
After the first user access, the user must accept the terms of conditions and then access the Synapse Prescription.

### Access keys
Memed offers two types of access keys:

- ** API-KEY ** - Public key, may appear on the front-end. Allows the app to access the search and user registration services.
- ** SECRET-KEY ** - Private key, should only be on the back-end. When used together with the API-KEY, allows prescriptions sending.

### Integrating with the API
In order for the user (health professional with CRM) to use Memed's prescription, it's necessary to register it in the Memed database, following the flow:

- The partner sends a request (specified below) with the user data to Memed
- Memed responds with a token and a user ID
- The partner stores the token and the user ID, for future use on the front-end

Request example using cURL:
```curl
curl -X POST \
  'http://local.api.memed.com.br/v1/sinapse-prescricao/usuarios?api-key=API-KEY&secret-key=SECRET-KEY' \
  -H 'Accept: application/vnd.api+json' \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -d '{
     "data": {
       "type": "usuarios",
         "attributes": {
           "nome": "José",
           "data_nascimento": "11/11/2011",
           "sobrenome": "da Silva",
             "cpf": "000.000.000-00",
             "email": "meu@email.com.br",
             "uf": "SP",
             "sexo": "M",
             "crm": "6231232249"
           },
           "relationships": {
             "cidade": {
                "data": { "type": "cidades", "id": "5213" }
              },
             "especialidade": {
                "data": { "type": "especialidades", "id": "50" }
              }
           }
      }
 }'
```

Attributes description (ALL THE ATTRIBUTES ARE REQUIRED):
```
User attributes:

nome - User first name
sobrenome - User last name
data_nascimento - User birthday, brazilian date pattern (DD/MM/YYYY)
cpf - User CPF, with punctuation (XXX.XXX.XXX-XX)
crm - User CRM, only numbers
uf - User UF (this is the same UF from the CRM)
email - User e-mail
sexo - User gender (M or F)

User relationships:

cidade (relationships.cidade.id) - City ID - The city where the user works
especialidade (relationships.especialidade.id) - Medical specialty ID
```

To find the city and specialty IDs, it's necessary to make two requests:

Lists all the medical specialities
```
curl -X GET -H "Accept: application/json" "https://api.memed.com.br/v1/especialidades"
```

Lists all the brazilian cities with "Campinas" word in the name
```
curl -X GET -H "Accept: application/json" "http://api.memed.com.br/v1/cidades?filter[q]=Campinas"
```

## Integrating with the front-end

To install the prescription module on your platform, you need to define a DOM element with the ID `memed-prescricao`, and then include the script which will load all necessary dependencies:

```
<a href="#" id="memed-prescricao">Prescribe</a>

<script
    type="text/javascript"
    src="http://memed.com.br/modulos/plataforma.sinapse-prescricao/build/sinapse-prescricao.min.js"
    data-token="USER_TOKEN"
    data-color="#576cff">
</script>
```

With the `data-color` attribute, you can customize the primary color, making the Synapse Prescription more similar to your product.

### Event listening
You can listen to events dispached from the Memed modules:

#### When the prescription module is closed
```js
MdSinapsePrescricao.event.add(
    'core:moduleHide',
    function closed(module) {
        if(module.moduleName === 'plataforma.prescricao') {
            console.log('====== Module closed ======', module);
        }
    }
);
```

#### When a drug is added to the prescription
```js
MdSinapsePrescricao.event.add('medicamentoAdicionado', function callback(drug) {
  // The drug object:
  // {
  //    "alto_custo":false,
  //    "composicao":"Princípio Ativo 1 + Princípio Ativo 2",
  //    "controle_especial":false,
  //    "descricao":"Ácido Ascórbico",
  //    "fabricante":"Sundown Vitaminas",
  //    "forma_fisica":"Cápsula",
  //    "id":"a123123123",
  //    "nome":"Vitamina C, comprimido (100un)",
  //    "quantidade":1,
  //    "tipo":"dermocosmético",
  //  }
});
```

## Capturing a prescription
When the user finishes a prescription, it's possible do capture the prescription ID:
```javascript
MdHub.event.add('prescricaoSalva', function prescricaoSalvaCallback(prescriptionId) {
	// Here you can send the prescription ID to your back-end, to get more info of the prescription
	functionToObtainMoreInformation(prescriptionId)l
});
```

On the back-end, to get the prescrition attributes, you can just send a request to the Memed's API:

```bash
curl -X GET 'http://local.api.memed.com.br/v1/prescricoes/PRESCRIPTION_ID?token=USER_TOKEN' -H 'accept: application/json'
```

The response will be like:

```json
{
    "data": {
        "type": "prescricoes",
        "attributes": {
            "data": "11/04/2017",
            "horario": "17:34:32",
            "medicamentos": [
                {
                    "id": "d2916",
                    "nome": "Creme Corretivo Dia, creme (40mL)",
                    "descricao": "Melanostatine + Vitamina PP + Derivados de vitamina C",
                    "posologia": "<p>Usar 1x ao dia</p>",
                    "quantidade": 1,
                    "fabricante": "Topicrem",
                    "titularidade": "Dermocosmético",
                    "tipo": "dermocosmético",
                }
            ],
            "paciente": {
                "id": 64414,
                "nome": "Gabriel",
            },
        },
        "id": 1234
    }
}
```

## Credits

:heart: Memed SA ([memed.com.br](https://memed.com.br))
