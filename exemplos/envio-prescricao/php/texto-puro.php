<?php
// Substitua as variáveis abaixo
$apiKey = 'SUA_API_KEY';
$secretKey = 'SUA_SECRET_KEY';

$prescricao = '
Nome: João da Silva

Roacutan 20mg, Cápsula (30un) Roche
Tomar 2x ao dia

Vitamina C, comprimido (100un) Sundown Vitaminas
Tomar 1x por semana
';

$usuarioId = '1234';
$usuarioCidade = 'São Paulo';
$usuarioEstado = 'SP';
$usuarioDataNascimento = '30/12/1900';
$usuarioEspecialidade = 'Dermatologia';
$usuarioSexo = 'F';
$usuarioProfissao = 'Médico';

// Início do código para envio do request para a API da Memed
$jsonData = [
    'data' => [
        'type' => 'prescricoes',
        'attributes' => [
            'texto' => $prescricao,
            'usuario' => [
                'id' => $usuarioId,
                'cidade' => $usuarioCidade,
                'data_nascimento' => $usuarioDataNascimento,
                'estado' => $usuarioEstado,
                'especialidade' => $usuarioEspecialidade,
                'sexo' => $usuarioSexo,
                'profissao' => $usuarioProfissao
            ]
        ]
    ]
];

$curl = curl_init();

curl_setopt_array($curl, array(
    CURLOPT_URL => 'https://api.memed.com.br/v1/prescricoes?api-key=' . $apiKey . '&secret-key=' . $secretKey,
    CURLOPT_RETURNTRANSFER => true,
    CURLOPT_TIMEOUT => 30,
    CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
    CURLOPT_CUSTOMREQUEST => 'POST',
    CURLOPT_POSTFIELDS => json_encode($jsonData),
    CURLOPT_HTTPHEADER => array(
        'accept: application/vnd.api+json',
        'cache-control: no-cache',
        'content-type: application/json',
    ),
));

$response = curl_exec($curl);
$err = curl_error($curl);

curl_close($curl);

// Para desenvolvimento, deixamos o código abaixo, assim é possível conferir a resposta da API da Memed
if ($err) {
    echo 'cURL Error #:' . $err;
} else {
    echo $response;
}
