<?php
declare(strict_types=1);

function escapar($valor): string
{
    return htmlspecialchars((string) $valor, ENT_QUOTES, 'UTF-8');
}

function iconeSvg(string $nome, string $classe = ''): string
{
    $icones = [
        'bed', 'calendar', 'camera', 'card', 'chat', 'check', 'clock', 'droplet',
        'food', 'heart', 'home', 'medical', 'paw', 'pin', 'pix', 'plant', 'shield',
        'sofa', 'sparkle', 'star', 'walk',
    ];

    if (!in_array($nome, $icones, true)) {
        return '';
    }

    $classe = $classe ? ' ' . escapar($classe) : '';
    return '<svg class="icone-svg' . $classe . '" aria-hidden="true" focusable="false"><use href="#icone-' . escapar($nome) . '"></use></svg>';
}

function definirAviso(string $texto): void
{
    $_SESSION['flash'] = $texto;
}

function avisoFlash(): string
{
    $aviso = $_SESSION['flash'] ?? '';
    unset($_SESSION['flash']);
    return $aviso ? '<p class="aviso-interacao" role="status">' . escapar($aviso) . '</p>' : '';
}

function redirecionar(string $pagina): void
{
    header('Location: ' . $pagina, true, 303);
    exit;
}

function csrfToken(): string
{
    if (empty($_SESSION['csrf_token'])) {
        $_SESSION['csrf_token'] = bin2hex(random_bytes(32));
    }
    return $_SESSION['csrf_token'];
}

function csrfCampo(): string
{
    return '<input type="hidden" name="csrf_token" value="' . escapar(csrfToken()) . '">';
}

function moeda(float $valor): string
{
    return 'R$ ' . number_format($valor, 2, ',', '.');
}

function dataCurta(string $data): string
{
    try {
        return (new DateTimeImmutable($data))->format('d/m/Y');
    } catch (Throwable $erro) {
        return $data;
    }
}

function dataPorExtenso(string $data, bool $comDia = true): string
{
    try {
        $objeto = new DateTimeImmutable($data);
    } catch (Throwable $erro) {
        return $data;
    }
    $dias = [1 => 'segunda-feira', 2 => 'terça-feira', 3 => 'quarta-feira', 4 => 'quinta-feira', 5 => 'sexta-feira', 6 => 'sábado', 7 => 'domingo'];
    $meses = [1 => 'janeiro', 2 => 'fevereiro', 3 => 'março', 4 => 'abril', 5 => 'maio', 6 => 'junho', 7 => 'julho', 8 => 'agosto', 9 => 'setembro', 10 => 'outubro', 11 => 'novembro', 12 => 'dezembro'];
    $texto = $objeto->format('j') . ' de ' . $meses[(int) $objeto->format('n')] . ' de ' . $objeto->format('Y');
    return $comDia ? $dias[(int) $objeto->format('N')] . ', ' . $texto : $texto;
}

function comprimentoTexto(string $texto): int
{
    return function_exists('mb_strlen') ? mb_strlen($texto, 'UTF-8') : strlen($texto);
}
