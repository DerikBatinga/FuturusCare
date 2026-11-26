<?php
declare(strict_types=1);

function arquivosEnviados(string $campo): array
{
    $arquivo = $_FILES[$campo] ?? null;
    if (!$arquivo || !isset($arquivo['error'])) {
        return [];
    }

    if (!is_array($arquivo['error'])) {
        return [[
            'name' => $arquivo['name'] ?? '',
            'tmp_name' => $arquivo['tmp_name'] ?? '',
            'error' => $arquivo['error'],
            'size' => $arquivo['size'] ?? 0,
        ]];
    }

    $arquivos = [];
    foreach ($arquivo['error'] as $indice => $erro) {
        $arquivos[] = [
            'name' => $arquivo['name'][$indice] ?? '',
            'tmp_name' => $arquivo['tmp_name'][$indice] ?? '',
            'error' => $erro,
            'size' => $arquivo['size'][$indice] ?? 0,
        ];
    }
    return $arquivos;
}

function salvarImagensEnviadas(string $campo, string $pasta, int $limite = 1): array
{
    $arquivos = array_values(array_filter(
        arquivosEnviados($campo),
        static fn(array $arquivo): bool => (int) $arquivo['error'] !== UPLOAD_ERR_NO_FILE
    ));
    if (!$arquivos) {
        return [];
    }
    if ($limite < 1) {
        throw new RuntimeException('A galeria já atingiu o limite de 12 fotos.');
    }
    if (count($arquivos) > $limite) {
        throw new RuntimeException('Selecione no máximo ' . $limite . ' imagem(ns) por envio.');
    }

    $formatos = [
        'image/jpeg' => 'jpg',
        'image/png' => 'png',
        'image/webp' => 'webp',
    ];
    $validados = [];
    foreach ($arquivos as $arquivo) {
        if (in_array((int) $arquivo['error'], [UPLOAD_ERR_INI_SIZE, UPLOAD_ERR_FORM_SIZE], true)) {
            throw new RuntimeException('Cada imagem deve ter no máximo 5 MB.');
        }
        if ((int) $arquivo['error'] !== UPLOAD_ERR_OK) {
            throw new RuntimeException('Não foi possível enviar uma das imagens. Tente novamente.');
        }
        if ((int) $arquivo['size'] <= 0 || (int) $arquivo['size'] > 5 * 1024 * 1024) {
            throw new RuntimeException('Cada imagem deve ter no máximo 5 MB.');
        }
        $dimensoes = @getimagesize((string) $arquivo['tmp_name']);
        $mime = $dimensoes['mime'] ?? '';
        if (!$dimensoes || !isset($formatos[$mime])) {
            throw new RuntimeException('Use imagens JPG, PNG ou WebP.');
        }
        if ((int) $dimensoes[0] > 8000 || (int) $dimensoes[1] > 8000) {
            throw new RuntimeException('A imagem deve ter no máximo 8.000 × 8.000 pixels.');
        }
        $validados[] = ['arquivo' => $arquivo, 'extensao' => $formatos[$mime]];
    }

    $pasta = preg_replace('/[^a-z0-9_-]/i', '', $pasta) ?: 'geral';
    $diretorioRelativo = 'uploads/' . $pasta;
    $diretorio = PUBLIC_PATH . '/' . $diretorioRelativo;
    if (!is_dir($diretorio) && !mkdir($diretorio, 0755, true) && !is_dir($diretorio)) {
        throw new RuntimeException('Não foi possível preparar a pasta das imagens.');
    }

    $salvos = [];
    foreach ($validados as $validado) {
        $nome = bin2hex(random_bytes(16)) . '.' . $validado['extensao'];
        $destino = $diretorio . '/' . $nome;
        if (!move_uploaded_file((string) $validado['arquivo']['tmp_name'], $destino)) {
            foreach ($salvos as $arquivoSalvo) {
                @unlink(PUBLIC_PATH . '/' . $arquivoSalvo);
            }
            throw new RuntimeException('Não foi possível salvar uma das imagens.');
        }
        $salvos[] = $diretorioRelativo . '/' . $nome;
    }
    return $salvos;
}

function imagemPerfil(array $usuario): string
{
    return (string) ($usuario['foto'] ?? '');
}

function imagemPet(array $pet): string
{
    return (string) ($pet['foto'] ?? 'assets/img/pet-home-destaque.png');
}

function removerImagemGerenciada(string $caminho): void
{
    if (!preg_match('#^uploads/(perfis|pets|galerias)/[a-f0-9]{32}\.(jpg|png|webp)$#', $caminho)) {
        return;
    }
    $arquivo = PUBLIC_PATH . '/' . $caminho;
    if (is_file($arquivo)) {
        @unlink($arquivo);
    }
}
