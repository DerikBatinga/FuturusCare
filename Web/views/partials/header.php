<?php
$tituloPagina = $tituloPagina ?? 'FuturusCare';
$paginaAtual = $paginaAtual ?? '';
$usuarioCabecalho = usuarioAtual();
$rotuloEntrar = $usuarioCabecalho ? 'Minha conta' : 'Entrar';
$navegacao = [
    'index.php' => 'Início',
    'servicos.php' => 'Serviços',
    'profissionais.php' => 'Profissionais',
    'sobre.php' => 'Sobre',
];
$destinoConta = !$usuarioCabecalho
    ? 'login.php'
    : ($usuarioCabecalho['tipo'] === 'profissional' ? 'painel.php' : 'tutorpainel.php');
$ancorasHome = [
    'servicos.php' => '#servicos',
    'profissionais.php' => '#como-funciona',
    'sobre.php' => '#sobre',
];
?>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><?= htmlspecialchars($tituloPagina, ENT_QUOTES, 'UTF-8') ?></title>
    <link rel="stylesheet" href="assets/css/style.css">
</head>
<body>
<svg class="icone-sprite" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
    <symbol id="icone-bed" viewBox="0 0 24 24"><path d="M3 20V5m0 10h18v5M3 10h6a3 3 0 0 1 3 3v2m0-5h4a5 5 0 0 1 5 5"/><path d="M6 7h.01"/></symbol>
    <symbol id="icone-calendar" viewBox="0 0 24 24"><rect x="3" y="5" width="18" height="16" rx="2"/><path d="M16 3v4M8 3v4M3 10h18"/></symbol>
    <symbol id="icone-camera" viewBox="0 0 24 24"><path d="M4 7h3l1.5-2h7L17 7h3a1 1 0 0 1 1 1v11H3V8a1 1 0 0 1 1-1Z"/><circle cx="12" cy="13" r="3.5"/></symbol>
    <symbol id="icone-card" viewBox="0 0 24 24"><rect x="2.5" y="5" width="19" height="14" rx="2"/><path d="M3 10h18M7 15h3"/></symbol>
    <symbol id="icone-chat" viewBox="0 0 24 24"><path d="M20 11.5a7.5 7.5 0 0 1-7.5 7.5H6l-3 2v-6.5a7.5 7.5 0 1 1 17-3Z"/><path d="M8 12h.01M12 12h.01M16 12h.01"/></symbol>
    <symbol id="icone-check" viewBox="0 0 24 24"><path d="m5 12 4 4L19 6"/></symbol>
    <symbol id="icone-clock" viewBox="0 0 24 24"><circle cx="12" cy="12" r="9"/><path d="M12 7v5l3.5 2"/></symbol>
    <symbol id="icone-droplet" viewBox="0 0 24 24"><path d="M12 3C9 7.5 6 10.5 6 14a6 6 0 0 0 12 0c0-3.5-3-6.5-6-11Z"/><path d="M9.5 15a2.5 2.5 0 0 0 2.5 2.5"/></symbol>
    <symbol id="icone-food" viewBox="0 0 24 24"><path d="M4 12h16l-2 7H6l-2-7ZM3 12h18M8 8c0-1.5 1-2 1-3m4 3c0-1.5 1-2 1-3m3 3c0-1.5 1-2 1-3"/></symbol>
    <symbol id="icone-heart" viewBox="0 0 24 24"><path d="M20.8 8.8c0 5.2-8.8 11-8.8 11s-8.8-5.8-8.8-11A4.8 4.8 0 0 1 12 6.3a4.8 4.8 0 0 1 8.8 2.5Z"/></symbol>
    <symbol id="icone-home" viewBox="0 0 24 24"><path d="m3 10 9-7 9 7v10a1 1 0 0 1-1 1h-6v-7h-4v7H4a1 1 0 0 1-1-1V10Z"/></symbol>
    <symbol id="icone-medical" viewBox="0 0 24 24"><path d="M9 3H5a2 2 0 0 0-2 2v4h6V3Zm6 0h4a2 2 0 0 1 2 2v4h-6V3ZM3 15v4a2 2 0 0 0 2 2h4v-6H3Zm12 6h4a2 2 0 0 0 2-2v-4h-6v6Z"/><path d="M10 12h4m-2-2v4"/></symbol>
    <symbol id="icone-paw" viewBox="0 0 24 24"><ellipse cx="6.1" cy="8.2" rx="2" ry="2.6" fill="currentColor" stroke="none"/><ellipse cx="11.9" cy="5.8" rx="2" ry="2.6" fill="currentColor" stroke="none"/><ellipse cx="17.8" cy="8.2" rx="2" ry="2.6" fill="currentColor" stroke="none"/><ellipse cx="20" cy="13.2" rx="1.8" ry="2.3" fill="currentColor" stroke="none"/><path d="M12 11c-3.2 0-7 4.4-7 7.1A2.9 2.9 0 0 0 8 21c1.5 0 2.4-.8 4-.8s2.5.8 4 .8a2.9 2.9 0 0 0 3-2.9c0-2.7-3.8-7.1-7-7.1Z" fill="currentColor" stroke="none"/></symbol>
    <symbol id="icone-pin" viewBox="0 0 24 24"><path d="M20 10c0 5-8 11-8 11S4 15 4 10a8 8 0 1 1 16 0Z"/><circle cx="12" cy="10" r="2.5"/></symbol>
    <symbol id="icone-pix" viewBox="0 0 24 24"><path d="m8.7 4.7 3.3-3.3 3.3 3.3m-6.6 14.6 3.3 3.3 3.3-3.3M4.7 8.7 1.4 12l3.3 3.3m14.6-6.6 3.3 3.3-3.3 3.3M8.7 4.7 4.7 8.7m10.6-4 4 4m-10.6 6.6-4 4m10.6-4 4 4M8.7 4.7 12 8l3.3-3.3M4.7 8.7 8 12l-3.3 3.3m10.6-3.3 3.3-3.3m0 6.6L15.3 12M8.7 19.3 12 16l3.3 3.3"/></symbol>
    <symbol id="icone-plant" viewBox="0 0 24 24"><path d="M12 21v-9m0 3c-5 0-7-3-7-7 4 0 7 2 7 7Zm0-2c0-5 3-8 8-8 0 5-3 8-8 8Z"/></symbol>
    <symbol id="icone-shield" viewBox="0 0 24 24"><path d="M12 22s8-4 8-11V5l-8-3-8 3v6c0 7 8 11 8 11Z"/><path d="m9 12 2 2 4-4"/></symbol>
    <symbol id="icone-sofa" viewBox="0 0 24 24"><path d="M5 11V7a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2v4M4 19v-2m16 2v-2M3 15v-2a2 2 0 0 1 4 0v2h10v-2a2 2 0 0 1 4 0v2a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2Z"/></symbol>
    <symbol id="icone-sparkle" viewBox="0 0 24 24"><path d="m12 3 1.8 5.2L19 10l-5.2 1.8L12 17l-1.8-5.2L5 10l5.2-1.8L12 3Zm7 12 .9 2.1L22 18l-2.1.9L19 21l-.9-2.1L16 18l2.1-.9L19 15ZM5 2l.7 1.8L7.5 4.5l-1.8.7L5 7l-.7-1.8-1.8-.7 1.8-.7L5 2Z"/></symbol>
    <symbol id="icone-star" viewBox="0 0 24 24"><path d="m12 3 2.8 5.7 6.2.9-4.5 4.4 1.1 6.2-5.6-3-5.6 3 1.1-6.2L3 9.6l6.2-.9L12 3Z"/></symbol>
    <symbol id="icone-walk" viewBox="0 0 24 24"><path d="M8 4.5c1.3 0 2.2 1.1 2.2 2.5S9.3 9.5 8 9.5 5.8 8.4 5.8 7 6.7 4.5 8 4.5Zm.2 7c2.1 0 3.8 2.6 3.8 5.3 0 1.7-.8 2.7-2 2.7-1.5 0-2.4-1.3-4-1.3-1.2 0-2 .8-2.7.8-1 0-1.5-.7-1.5-1.8 0-2.7 3.2-5.7 6.4-5.7ZM17 3.5c1.2 0 2.1 1 2.1 2.3s-.9 2.3-2.1 2.3-2.1-1-2.1-2.3.9-2.3 2.1-2.3Zm.5 6.7c2.7 0 5 2.7 5 5 0 1.2-.6 1.8-1.5 1.8-.8 0-1.6-.6-2.8-.6-1.3 0-2.2 1.1-3.3 1.1-.9 0-1.5-.8-1.5-2 0-2.4 1.9-5.3 4.1-5.3Z"/></symbol>
</svg>
<a class="pular-conteudo" href="#conteudo">Pular para o conteúdo</a>
<header class="cabecalho container">
    <a class="marca" href="index.php" aria-label="FuturusCare — início">
        <img src="assets/img/logo-home.png" alt="FuturusCare" width="174" height="41">
    </a>
    <nav class="navegacao" aria-label="Navegação principal">
        <?php foreach ($navegacao as $arquivo => $rotulo): ?>
            <?php if (is_file(PUBLIC_PATH . '/' . $arquivo)): ?>
                <a href="<?= $arquivo ?>" <?= $paginaAtual === $arquivo ? 'aria-current="page"' : '' ?>><?= $rotulo ?></a>
            <?php elseif ($paginaAtual === 'index.php' && isset($ancorasHome[$arquivo])): ?>
                <a href="<?= $ancorasHome[$arquivo] ?>"><?= $rotulo ?></a>
            <?php else: ?>
                <span aria-disabled="true"><?= $rotulo ?></span>
            <?php endif; ?>
        <?php endforeach; ?>
    </nav>
    <div class="cabecalho-acoes">
        <?php if (is_file(PUBLIC_PATH . '/' . $destinoConta)): ?>
            <a class="acao-entrar" href="<?= $destinoConta ?>"><?= $rotuloEntrar ?></a>
        <?php elseif (is_file(PUBLIC_PATH . '/tutorpainel.php')): ?>
            <a class="acao-entrar" href="tutorpainel.php"><?= $rotuloEntrar ?></a>
        <?php else: ?>
            <span class="acao-entrar" aria-disabled="true"><?= $rotuloEntrar ?></span>
        <?php endif; ?>
        <?php if (is_file(PUBLIC_PATH . '/servico.php')): ?>
            <a class="acao-cuidador" href="servico.php">Encontrar cuidador</a>
        <?php elseif ($paginaAtual === 'index.php'): ?>
            <a class="acao-cuidador" href="#servicos">Encontrar cuidador</a>
        <?php else: ?>
            <span class="acao-cuidador" aria-disabled="true">Encontrar cuidador</span>
        <?php endif; ?>
        <?php if (usuarioAtual()): ?>
            <form method="post" action="sair.php" class="cabecalho-sair-form">
                <input type="hidden" name="acao" value="logout">
                <?= csrfCampo() ?>
                <button class="acao-entrar" type="submit">Sair</button>
            </form>
        <?php endif; ?>
    </div>
</header>
<?= avisoFlash() ?>
