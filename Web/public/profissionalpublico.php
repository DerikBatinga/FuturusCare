<?php
require_once __DIR__ . '/../src/bootstrap.php';
$usuarioLogado = usuarioAtual();
$perfilAlvoId = isset($_GET['id'])
    ? (int) $_GET['id']
    : (($usuarioLogado['tipo'] ?? '') === 'profissional' ? (int) $usuarioLogado['id'] : 2);
$perfilProfissional = null;
foreach (dados('profissionais') as $perfil) { if ((int) $perfil['usuario_id'] === $perfilAlvoId) { $perfilProfissional = $perfil; break; } }
$perfilProfissional ??= dados('profissionais')[0];
$usuarioProfissional = null;
foreach (dados('usuarios') as $usuario) { if ((int) $usuario['id'] === (int) $perfilProfissional['usuario_id']) { $usuarioProfissional = $usuario; break; } }
$nomeProfissional = $usuarioProfissional['nome'];
$donoPerfil = $usuarioLogado && (int) $usuarioLogado['id'] === (int) $usuarioProfissional['id'];
$dataCadastroProfissional = dataCurta((string) ($usuarioProfissional['data_cadastro'] ?? ''));
$servicosPerfil = $perfilProfissional['servicos'] ?? [];
$tituloPagina = 'Perfil de ' . $nomeProfissional . ' | FuturusCare';
$paginaAtual = 'profissionalpublico.php';
$caracteristicas = [
    ['icone' => 'home', 'texto' => 'Mora em casa', 'cor' => 'lilas'],
    ['icone' => 'paw', 'texto' => 'Tem outros pets', 'cor' => 'laranja'],
    ['icone' => 'plant', 'texto' => 'Espaço externo', 'cor' => 'verde'],
    ['icone' => 'sofa', 'texto' => 'Pode subir no sofá', 'cor' => 'rosa'],
    ['icone' => 'bed', 'texto' => 'Pode subir na cama', 'cor' => 'rosa'],
    ['icone' => 'medical', 'texto' => 'Veterinário próximo', 'cor' => 'lilas'],
];
$habilidades = [
    'Primeiros socorros', 'Aplica medicação oral', 'Aceita pets idosos',
    'Aceita pets especiais', 'Exercita pets', 'Transporte de emergência',
];
$galeria = array_values($perfilProfissional['galeria'] ?? []);
$diasSemana = ['D', 'S', 'T', 'Q', 'Q', 'S', 'S'];
$primeiroDia = (int) (new DateTimeImmutable('2026-10-01'))->format('w');
$diasNoMes = (int) (new DateTimeImmutable('2026-10-01'))->format('t');
renderizarPagina('profissionalpublico.php');
