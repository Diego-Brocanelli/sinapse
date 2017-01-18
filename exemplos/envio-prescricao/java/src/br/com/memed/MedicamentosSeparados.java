package br.com.memed;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class MedicamentosSeparados {

	public static void main ( String [] arguments )
    {
		try {
			// Atenção: A Memed utiliza certificados SSL providos pelo Let's Encrypt, que foi adicionado como Trusted CA
			// a partir do Oracle Java 8u101. Caso utilize uma versão inferior, adicione manualmente os certificados ao seus sistema
			// https://letsencrypt.org/certificates/
			
			// Substitua as variáveis abaixo
			String apiKey = "SUA_API_KEY";
			String secretKey = "SUA_SECRET_KEY";
			
			List<Medicamento> listaMedicamentos = new ArrayList<Medicamento>();
			
			// Um medicamento da Memed possui ID, ao envia-lo para a Memed, será identificado e ajudará
			// no melhoramento da busca para o usuário
			Medicamento medicamento1 = new Medicamento();
			medicamento1.id = "a123123123";
			medicamento1.posologia = "Tomar 2x ao dia";
			medicamento1.quantidade = 2;
			
			listaMedicamentos.add(medicamento1);
			
			// O usuário pode criar um medicamento (chamamos de Medicamento Custom), sendo assim, ele não possui ID,
			// mas possui nome
			Medicamento medicamento2 = new Medicamento();
			medicamento2.nome = "Vitamina C, comprimido (100un) Sundown";
			medicamento2.posologia = "Tomar 1x ao dia";
			medicamento2.quantidade = 1;
			
			listaMedicamentos.add(medicamento2);
			
			String usuarioId = "1234";
	        String usuarioCidade = "São Paulo";
	        String usuarioEstado = "SP";
	        String usuarioDataNascimento = "30/12/1900";
	        String usuarioEspecialidade = "Dermatologia";
	        String usuarioSexo = "F";
	        String usuarioProfissao = "Médico";
	        
	        // Início do código para envio do request para a API da Memed
	        JSONObject usuario = new JSONObject();
	        usuario.put("id", usuarioId);
	        usuario.put("cidade", usuarioCidade);
	        usuario.put("estado", usuarioEstado);
	        usuario.put("data_nascimento", usuarioDataNascimento);
	        usuario.put("especialidade", usuarioEspecialidade);
	        usuario.put("sexo", usuarioSexo);
	        usuario.put("profissao", usuarioProfissao);
	        
	        JSONObject prescricaoAttributes = new JSONObject();
	        prescricaoAttributes.put("usuario", usuario);
	        
	        JSONArray medicamentosJSON = new JSONArray();
	        
	        for(int i = 0 ; i < listaMedicamentos.size(); i++){
	        	Medicamento medicamento = listaMedicamentos.get(i);
	        	JSONObject medicamentoJSON = new JSONObject();
	        	
	        	if (medicamento.id != null && ! medicamento.id.isEmpty()) {
	        		medicamentoJSON.put("id", medicamento.id);
	        	}
	        	
	        	if (medicamento.nome != null && ! medicamento.nome.isEmpty()) {
	        		medicamentoJSON.put("nome", medicamento.nome);
	        	}
	        	
	        	if (medicamento.posologia != null && ! medicamento.posologia.isEmpty()) {
	        		medicamentoJSON.put("posologia", medicamento.posologia);
	        	}
	        	
	        	if (medicamento.quantidade > 0) {
	        		medicamentoJSON.put("quantidade", medicamento.quantidade);
	        	}
	        	
	        	medicamentosJSON.put(i, medicamentoJSON);
	        }
	        
	        prescricaoAttributes.put("medicamentos", medicamentosJSON);
	        
	        JSONObject prescricao = new JSONObject();
	        prescricao.put("type", "prescricoes");
	        prescricao.put("attributes", prescricaoAttributes);
	        
	        JSONObject data = new JSONObject();
	        data.put("data", prescricao);
	        
			URL url = new URL("https://api.memed.com.br/v1/prescricoes?api-key=" + apiKey + "&secret-key=" + secretKey);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
	        conn.setDoOutput(true);
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Accept", "application/json");
	        conn.setRequestProperty("Content-Type", "application/json");
	
	        OutputStream os = conn.getOutputStream();
	        os.write(data.toString().getBytes(StandardCharsets.UTF_8));
	        os.flush();
	        os.close();
	        
	        InputStream in = conn.getInputStream();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	        StringBuilder result = new StringBuilder();
	        String line;
	        
	        while((line = reader.readLine()) != null) {
	            result.append(line);
	        }
	        
	        // Resposta da API da Memed
	        System.out.println(result.toString());
	        
	        // Se o código HTTP de resposta for 201, a prescrição foi criada com sucesso
	        System.out.println(conn.getResponseCode());
	        
	        conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
    }

}
