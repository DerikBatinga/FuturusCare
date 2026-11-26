<?php
require_once __DIR__ . '/../src/bootstrap.php';
$tituloPagina = 'Diário do Thor | FuturusCare';
$paginaAtual = 'diario.php';
$usuarioDiario = usuarioAtual();
$petsVisiveis = $usuarioDiario['tipo'] === 'tutor' ? usuariosDoTutor((int) $usuarioDiario['id']) : [];
if ($usuarioDiario['tipo'] === 'profissional') {
    $idsPet = [];
    foreach (agendamentosDoUsuario($usuarioDiario) as $atendimento) { $idsPet[] = (int) $atendimento['pet_id']; }
    $petsVisiveis = array_values(array_filter(dados('pets'), static fn(array $pet): bool => in_array((int) $pet['id'], $idsPet, true)));
}
$idsPetVisiveis = array_map(static fn(array $pet): int => (int) $pet['id'], $petsVisiveis);
$registrosOriginais = array_values(array_filter(dados('diario'), static fn(array $registro): bool => in_array((int) $registro['pet_id'], $idsPetVisiveis, true)));
$registros = array_map(static function ($registro) {
    $icone = ['Bem-estar' => 'heart', 'Alimentação' => 'food', 'Água' => 'droplet', 'Passeio' => 'walk', 'Saúde' => 'medical'];
    $registro['icone'] = $icone[$registro['categoria']] ?? 'paw';
    $registro['hora'] = $registro['horario'];
    $registro['texto'] = $registro['relato'];
    $autor = null;
    foreach (dados('usuarios') as $usuario) { if ((int) $usuario['id'] === (int) $registro['autor_id']) { $autor = $usuario; break; } }
    $registro['autor'] = ($autor['nome'] ?? 'Pessoa da família') . (($autor['tipo'] ?? '') === 'profissional' ? ' · Profissional' : ' · Tutor');
    return $registro;
}, $registrosOriginais);
usort($registros, static fn(array $a, array $b): int => strcmp($b['data'] . $b['horario'], $a['data'] . $a['horario']));
$petDiario = $petsVisiveis[0] ?? null;
renderizarPagina('diario.php');
