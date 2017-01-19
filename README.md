# Memed Sinapse

Memed Sinapse é um plugin 100% gratuito da Memed que transforma qualquer campo de texto (inputs, textarea) em uma intuitiva ferramenta de suporte a prescrição médica.

Com um simples setup, o médico tem acesso a:
- Banco de medicamento mais completo e atualizado do Brasil, mais de 20 mil entre composições, apresentações comerciais e fórmulas manipuladas;
- Classificações farmacológicas de todos os medicamentos;
- Busca inteligente por princípio ativo, apresentações comerciais, forma física e tipo de medicamento;
- Memed Síntese, todas as informações de um medicamento;
- Melhorias e atualizações automáticas;


## Como integrar

- Escolha um campo de texto (\<input>, \<textarea>) e adicione a classe "memed-autocomplete".

```html
<textarea class="sua-classe outra-classe memed-autocomplete"></textarea>
```

- Adicione o script abaixo ao final do HTML (antes do **\</body>**), preenchendo os atributos "data-*" com os dados de seu usuário, eles serão utilizados para uma experiência mais personalizada.

```html
<script type="text/javascript" src="https://memed.com.br/modulos/plataforma.sinapse/build/sinapse.js" data-api-key="SUA_API_KEY" data-usuario="123" data-cidade="São Paulo" data-nascimento="30/12/1900" data-especialidade="Dermatologia" data-estado="SP" data-sexo="M" data-profissao="Médico"></script>
```

Após adicionar o script, já será possível ver o Memed Sinapse em ação:

![memed-sinapse-em-acao.gif](https://cloud.githubusercontent.com/assets/2197005/22080162/b344340e-dda5-11e6-841b-13486c61548c.gif)

Caso queira capturar os dados do medicamento inserido, você pode adicionar um callback javascript:

```javascript
MdSinapse.event.add('medicamentoAdicionado', function callback(medicamento) {
  // O objeto medicamento:
  // {
  //    "composicao":"Princípio Ativo 1 + Princípio Ativo 2",
  //    "descricao":"Ácido Ascórbico",
  //    "fabricante":"Sundown Vitaminas",
  //    "id":"a123123123",
  //    "nome":"Vitamina C, comprimido (100un)",
  //    "quantidade":1,
  //    "tipo":"dermocosmético",
  //  }
});
```

Obs: Não possui uma API KEY? Entre em contato com a gente através do e-mail [contato@memed.com.br](mailto:contato@memed.com.br)

- Quando um usuário salvar uma prescrição em seu prontuário, envie para a Memed, assim o Memed Sinapse ficará mais inteligente para o seu usuário.

O envio da prescrição deve feito através de um **REQUEST HTTP** para a API da Memed, a qual receberá um JSON com os dados da prescrição, conforme a documentação abaixo. Como forma de facilitar a integração, estão disponíveis exemplos em algumas linguagens de programação, assim como a especificação do request usando cURL (que pode ser executada no terminal de comando).

### Prescrição como texto puro:

É possível enviar a prescrição em forma de texto puro, o qual será processado pela Memed e terá seus atributos identificados e separados (paciente, medicamentos, posologias...).

Exemplo usando cURL:

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
' "https://api.memed.com.br/v1/prescricoes?api-key=SUA_API_KEY&secret-key=SUA_SECRET_KEY"
```

- [Exemplo PHP](exemplos/envio-prescricao/php/texto-puro.php)
- [Exemplo Java](exemplos/envio-prescricao/java/src/br/com/memed/TextoPuro.java)

### Prescrição com medicamentos separados:

É possível enviar a prescrição como um array de medicamentos, o que fornece uma precisão maior na identificação de cada medicamento e na separação de seus atributos.

Exemplo usando cURL:

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
' "https://api.memed.com.br/v1/prescricoes?api-key=SUA_API_KEY&secret-key=SUA_SECRET_KEY"
```

- [Exemplo PHP](exemplos/envio-prescricao/php/medicamentos-separados.php)
- [Exemplo Java](exemplos/envio-prescricao/java/src/br/com/memed/MedicamentosSeparados.java)

## Creditos

:heart: Memed SA ([memed.com.br](https://memed.com.br))
