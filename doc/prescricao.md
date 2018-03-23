# Memed Sinapse Prescrição

Sinapse Prescrição é a ferramenta 100% gratuita da Memed que permite o seu site/plataforma/prontuário ter uma prescrição inteligente com diversas funcionalidades:

- Protocolos - Praticidade e velocidade na rotina de atendimentos.
- Minhas Posologias - Agilidade para o médico prescrever com base em seus histórico.
- Impressão inteligente - Regras de impressão, controle de margem, logo customizado e impressão automática de receituários especiais.
- Envio por SMS - Facilita a vida do paciente, entregando agilidade e permitindo a comparação de preços.
- Banco de medicamentos - O mais completo do Brasil, com informações constantemente curadas pela nossa equipe.

## Como integrar
A integração é feita em dois momentos: implementando o cadastro do usuário (profissional da saúde com CRM) via API e depois implementando o Sinapse Prescrição (front-end) dentro de seu sistema.
Logo no primeiro acesso do usuário, o mesmo deverá aceitar os termos de condições e então poderá acessar o Sinapse Prescrição.

### Chaves de acesso
A Memed disponibiliza duas chaves de acesso:

- **API-KEY** - Chave pública, pode aparecer no front-end. Permite a aplicação acessar os serviços de busca e cadastro de usuário.
- **SECRET-KEY** - Chave privada, deve ficar somente no back-end. Quando usada junto com a API-KEY, permite o envio de prescrições.

### Integrando com a API
Para que o usuário (profissional da saúde com CRM) possa utilizar a prescrição da Memed, é necessário cadastrá-lo no banco de dados da Memed, seguindo o fluxo abaixo:

- O parceiro envia um request (especificado mais abaixo) com os dados do usuário para a Memed
- A Memed responde com um token e um ID de usuário
- O parceiro armazena o token e o ID em seu banco, para usar futuramente nas integrações

Exemplo do request usando cURL:
```curl
curl -X POST \
  'https://api.memed.com.br/v1/sinapse-prescricao/usuarios?api-key=API-KEY&secret-key=SECRET-KEY' \
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

Veja abaixo uma descrição de cada atributo, TODOS SÃO OBRIGATÓRIOS:
```
Atributos do usuário:

nome - Nome do usuário
sobrenome - Sobrenome do usuário
data_nascimento - Data de nascimento do usuário, no padrão brasileiro (DD/MM/YYYY)
cpf - CPF do usuário, com pontuação (XXX.XXX.XXX-XX)
crm - CRM do usuário, somente números
uf - UF do usuário (onde o CRM está cadastrado)
email - E-mail do usuário
sexo - Sexo do usuário (M ou F)

Relacionamentos do usuário:

cidade (relationships.cidade.id) - ID da cidade que o usuário atende
especialidade (relationships.especialidade.id) - ID da especialidade do usuário
```

Para encontrar os IDs da cidade e especialidade, é necessário fazer dois requests:

Lista de todas as especialidades
```
curl -X GET -H "Accept: application/json" "https://api.memed.com.br/v1/especialidades"
```

Lista de cidades, filtradas por um nome
```
curl -X GET -H "Accept: application/json" "http://api.memed.com.br/v1/cidades?filter[q]=Campinas"
```

## Integrando com o front-end
Para integrar o módulo de prescricão em sua plataforma é necessário definir um elemento com id `memed-prescricao` e depois incluir o script que irá carregar as dependências necessárias:

```
<a href="#" id="memed-prescricao">Prescrever</a>

<script
    type="text/javascript"
    src="http://memed.com.br/modulos/plataforma.sinapse-prescricao/build/sinapse-prescricao.min.js"
    data-token="TOKEN_DO_USUARIO_OBTIDO_NO_CADASTRO_VIA_API"
    data-color="#576cff">
</script>
```

Com a propriedade `data-color` você pode customizar a cor primária, deixando o Sinapse Prescrição mais parecido com o seu produto.

### Escutando eventos
Você pode assinar eventos que são disparados pelos módulos e implementa-los conforme necessidade em sua plataforma.

#### Quando o módulo de prescricão for fechado
```js
MdSinapsePrescricao.event.add(
    'core:moduleHide',
    function moduloFechado(modulo) {
        if(modulo.moduleName === 'plataforma.prescricao') {
            console.log('====== Módulo fechado ======', modulo);
        }
    }
);
```

#### Quando um medicamento for adicionado
Caso queira capturar os dados do medicamento inserido, você pode adicionar um callback javascript:
```js
MdSinapsePrescricao.event.add('medicamentoAdicionado', function callback(medicamento) {
  // O objeto medicamento:
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
## Capturando uma prescrição

Quando o usuário terminar uma prescrição, é possível capturar o ID da mesma, assim como pedir mais informações, através de um evento:

```javascript
MdHub.event.add('prescricaoSalva', function prescricaoSalvaCallback(idPrescricao) {
	// Aqui é possível enviar esse ID para seu back-end obter mais informações
	funcaoParaEnviarParaBackendObterMaisInformacoes(idPrescricao);
});
```

No back-end, para obter mais informações sobre a prescrição, basta enviar um request para a API da Memed, com o ID da prescrição e o token do usuário:

```bash
curl -X GET 'https://api.memed.com.br/v1/prescricoes/AQUI_VAI_O_ID_DA_PRESCRICAO?token=AQUI_VAI_O_TOKEN_DO_USUARIO' -H 'accept: application/json'
```

A resposta será como a abaixo:

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
