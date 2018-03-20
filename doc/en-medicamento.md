# Memed Sinapse Medicamento (Memed Synapse Drug)

Memed Synapse Drug is a 100% free Memed plugin which turns any text input (inputs, textarea) into an intuitive tool for medical prescription.

With a simple setup, the doctor has access to:
- The most complete and updated drugs database of Brazil, more than 20 thousand drugs;
- Pharmacological classification of all drugs;
- Smart search by active ingredient, physical form and type of drug;
- Memed Synthesis, all information about a drug;
- Automatic upgrades with improvements;

## Dictionary

- UF - The Federative Republic of Brazil is a union of 27 Federative Units (Portuguese: Unidades Federativas, UF) (ex: SP, RJ, MG)
- Medical Specialty - A specialty, or speciality, in medicine is a branch of medical practice (ex: Dermatologista [Dermatologist])

## How to integrate

- Choose a text input (\<input>, \<textarea>) and add the class "memed-autocomplete".

```html
<textarea class="your-class another-class memed-autocomplete"></textarea>
```

- Add the script at the end of your HTML (before **\</body>**), filling the attributes "data-\*" with the user data.

```html
<script type="text/javascript" src="//memed.com.br/modulos/plataforma.sinapse/build/sinapse.min.js"
 data-api-key="YOUR_API_KEY" data-usuario="123" data-cidade="São Paulo" data-nascimento="30/12/1900"
 data-especialidade="Dermatologia" data-estado="SP" data-sexo="M" data-profissao="Médico"
 data-width="900" data-font="Open Sans" data-color="#0A6FFF"></script>
```

### Access keys
Memed offers two types of access keys:

- ** API-KEY ** - Public key, may appear on the front-end. Allows the app to access the search and user registration services.
- ** SECRET-KEY ** - Private key, should only be on the back-end. When used together with the API-KEY, allows prescriptions sending.

### Single page Applications (Angular, Ember, Vue...)

If you don't have the user data at the moment of HTML rendering, you can define them after using Javascript, just add the attribute `data-init="manual"` to the script, as bellow:

```html
<script type="text/javascript" src="//memed.com.br/modulos/plataforma.sinapse/build/sinapse.min.js"
 data-api-key="YOUR_API_KEY" data-width="900" data-font="Open Sans" data-color="#0A6FFF"
 data-init="manual"></script>
```

On your javascript, after the user login, initialize the Synapse Drug with the user data:

```javascript
MdSinapse.init({
	cidade: 'São Paulo',
	nascimento: '30/12/1900',
	especialidade: 'Dermatologia',
	estado: 'SP',
	sexo: 'M',
	profissao: 'Médico',
  usuario: 1234
});
```

PS: If your user logout, you just need to call again the `MdSinapse.init` function to redefine the user data.

## Sinapse Medicamento Builder

<p align="left"><a href="https://memeddev.github.io/sinapse/builder.html" target="_blank"><img src="https://cloud.githubusercontent.com/assets/2197005/23463496/7c1e6e90-fe70-11e6-84d8-5a815c6b7890.png" alt="Memed Sinapse Medicamento Builder Logo" /></a></p>

You can generate the script using our Sinapse Medicamento Builder, a tool which help you to live testing some settings (like size, color, font-family). The tool is avaible at [https://memeddev.github.io/sinapse/builder.html](https://memeddev.github.io/sinapse/builder.html).

## Required settings and user attributes

After adding the script, it'll be possible to see the Memed Sinapse Medicamento in action:

![sinapse_setup_2](https://cloud.githubusercontent.com/assets/21063429/22213858/dba8a7c4-e17c-11e6-9501-bc5afa2b2eb9.gif)

The users of your platform don't need to be registered at Memed, but for a more personal use, the user data may be defined at the script attributes.

```
data-api-key [required] - Public key, the same for all the user.

User attributes:
data-usuario [required] - User ID (it's a unique identifier of the user based on some ID used in your platform, can be the hash of the ID)
data-cidade - User city (ex: São Paulo)
data-nascimento - User birthday, brazilian date pattern (DD/MM/YYYY)
data-especialidade - Medical specialty ID
data-estado - User UF
data-sexo - User gender (M or F)
data-profissao - User occupation

Style attributes:
data-color - Autocomplete font color (hex, ex: "#20afd6")
data-font - Autocomplete font family (options: "Arial, Helvetica", "Lato", "Nunito", "Open Sans", "Proxima Nova", "Roboto", "Verdana")
data-width - Autocomplete max width (in px)
```

If you want to listen the event of "autocomplete drug selected":

```javascript
MdSinapse.event.add('medicamentoAdicionado', function callback(drug) {
  // Drug object:
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

- When the user saves a prescription on your platform, you should send it to Memed, this will turn the Memed Sinapse Medicamento smarter to the user.

The sending of the prescription must be done through an **HTTP REQUEST** to the Memed's API, which will receive a JSON filled with the prescription data. To make an easier integration, examples are available is this documentation using some common programming languages, as well using cURL (which can be executed on the command line).

### Prescription as plain text

It's possible to send the prescription as plain text, which will be processed by Memed and will have its attributes identified and separated (drugs, posologies ...).

Example:

```bash
curl -X POST -H "Accept: application/json" -H "Content-Type: application/json" -d '
{
  "data": {
    "type": "prescricoes",
    "attributes": {
      "texto": "Nome: João da Silva\n\nRoacutan 20mg, Cápsula (30un) Roche\nTomar 2x ao dia\n\nVitamina C, comprimido (100un) Sundown Vitaminas\nTomar 1x por semana",
      "usuario": {
    	"id": "1234",
    	"cidade": "São Paulo",
    	"data_nascimento": "30/12/1900",
    	"especialidade": "Dermatologia",
    	"estado": "SP",
    	"sexo": "F",
    	"profissao": "Médico"
      }
    }
  }
}
' "https://api.memed.com.br/v1/prescricoes?api-key=YOUR_API_KEY&secret-key=YOUR_SECRET_KEY"
```

- [Postman](exemplos/postman/postman_collection.json)
- [PHP](exemplos/envio-prescricao/php/texto-puro.php)
- [Java](exemplos/envio-prescricao/java/src/br/com/memed/TextoPuro.java)

### Prescription as array of drugs:

It's possible to send the prescription as an array of drugs, which provides greater precision on identifying each drug and on separating its attributes.

Example:

```bash
curl -X POST -H "Accept: application/json" -H "Content-Type: application/json" -d '
{
  "data": {
    "type": "prescricoes",
    "attributes": {
      "medicamentos": [
        {
          "id": "a123123123",
          "posologia": "Tomar 2x ao dia",
          "quantidade": "2"
        },
        {
          "nome": "Vitamina C, comprimido (100un) Sundown",
          "posologia": "Tomar 1x ao dia,
          "quantidade": "1"
        }
      ],
      "usuario": {
    	"id": "1234",
    	"cidade": "São Paulo",
    	"data_nascimento": "30/12/1900",
    	"especialidade": "Dermatologia",
    	"estado": "SP",
    	"sexo": "F",
    	"profissao": "Médico"
      }
    }
  }
}
' "https://api.memed.com.br/v1/prescricoes?api-key=YOUR_API_KEY&secret-key=YOUR_SECRET_KEY"
```

- [Postman](exemplos/postman/postman_collection.json)
- [PHP](exemplos/envio-prescricao/php/medicamentos-separados.php)
- [Java](exemplos/envio-prescricao/java/src/br/com/memed/MedicamentosSeparados.java)

## Using only the Memed Síntese

If you want to only use the Memed Síntese (drugs attribute panel), add the Memed Sinapse Medicamento script using the `data-modulos="sintese"` attribute:

```html
<script type="text/javascript" src="//memed.com.br/modulos/plataforma.sinapse/build/sinapse.min.js"
 data-api-key="YOUR_API_KEY" data-usuario="123" data-cidade="São Paulo" data-nascimento="30/12/1900"
 data-especialidade="Dermatologia" data-estado="SP" data-sexo="M" data-profissao="Médico" data-width="900"
 data-modulos="sintese"></script>
```

To open the Memed Síntese, execute the Javascript code below, replacing `drug_id` to the drug ID obtained using our [API](http://integracao.api.memed.com.br/doc/parceiros).

```javascript
MdSinapse.command.send('plataforma.medicamento', 'verMedicamento', {
    id: 'drug_id'
});
```

## Showing just some autocomplete tabs

<p align="center"><img src="https://cloud.githubusercontent.com/assets/2197005/23523445/b35bd000-ff65-11e6-8c6d-5c0c5264c8e3.png" alt="Memed Autocomplete with Composições and Exames tabs enabled" /></p>

If you need to disable some autocomplete tabs, just add the `data-memed-categorias` attribute to your input field (\<input>, \<textarea>, \<div>):

```html
<input type="text" class="your-classe another-classe memed-autocomplete" data-memed-categorias="composicoes,exames">
```

The valid categories are: [industrializados, manipulados, composicoes, exames]

## Credits

:heart: Memed SA ([memed.com.br](https://memed.com.br))
