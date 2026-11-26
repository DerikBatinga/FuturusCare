<?php
declare(strict_types=1);

require_once dirname(__DIR__) . '/config/app.php';

if (session_status() !== PHP_SESSION_ACTIVE) {
    session_set_cookie_params([
        'httponly' => true,
        'secure' => !empty($_SERVER['HTTPS']) && $_SERVER['HTTPS'] !== 'off',
        'samesite' => 'Lax',
    ]);
    session_start();
}

if (!isset($_SESSION['fc'])) {
    $_SESSION['fc'] = require APP_ROOT . '/src/Repositories/mock.php';
}

// Completa sessões criadas antes da inclusão da data de cadastro.
$datasCadastroIniciais = [1 => '2026-06-05', 2 => '2026-06-05', 3 => '2026-07-12', 4 => '2026-08-21'];
foreach ($_SESSION['fc']['usuarios'] as &$usuarioSessao) {
    if (empty($usuarioSessao['data_cadastro'])) {
        $usuarioSessao['data_cadastro'] = $datasCadastroIniciais[(int) $usuarioSessao['id']] ?? date('Y-m-d');
    }
}
unset($usuarioSessao);

require_once APP_ROOT . '/src/Support/helpers.php';
require_once APP_ROOT . '/src/Repositories/MockRepository.php';
require_once APP_ROOT . '/src/Services/UploadService.php';
require_once APP_ROOT . '/src/Services/AuthService.php';
require_once APP_ROOT . '/src/Controllers/PageController.php';
require_once APP_ROOT . '/src/Services/RequestService.php';
