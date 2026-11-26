<?php
declare(strict_types=1);

function registrarErro(string $mensagem, string $pagina): void
{
    definirAviso($mensagem);
    redirecionar($pagina);
}

function exigirPerfil(array $tipos, string $paginaAtual): void
{
    $usuario = usuarioAtual();
    if (!$usuario) {
        $_SESSION['destino_depois_login'] = $paginaAtual;
        definirAviso('Entre na sua conta para continuar.');
        redirecionar('login.php');
    }
    if (!in_array($usuario['tipo'], $tipos, true)) {
        definirAviso('Este espaço não está disponível para o tipo da sua conta.');
        redirecionar($usuario['tipo'] === 'profissional' ? 'painel.php' : 'tutorpainel.php');
    }
}

function protegidaPorPerfil(string $pagina): ?array
{
    $rotas = [
        'tutorpainel.php' => ['tutor'], 'pets.php' => ['tutor'], 'edicao.php' => ['tutor'],
        'servico.php' => ['tutor'], 'cuidador.php' => ['tutor'], 'data.php' => ['tutor'],
        'revisao.php' => ['tutor'], 'confirmacao.php' => ['tutor'],
        'pagamento.php' => ['tutor'], 'mensagens.php' => ['tutor'],
        'agenda.php' => ['tutor', 'profissional'], 'diario.php' => ['tutor', 'profissional'],
        'painel.php' => ['profissional'], 'editarprofissional.php' => ['profissional'],
        'chatprofissional.php' => ['profissional'], 'registro.php' => ['profissional'],
    ];
    return $rotas[$pagina] ?? null;
}
