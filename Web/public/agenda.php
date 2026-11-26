<?php
require_once __DIR__ . '/../src/bootstrap.php';
$tituloPagina = 'Agendamentos | FuturusCare';
$paginaAtual = 'agenda.php';
$dias = [
    ['dia' => 'Seg', 'data' => '28', 'classe' => 'agendamento-dia-mes-anterior'],
    ['dia' => 'Ter', 'data' => '29', 'classe' => 'agendamento-dia-mes-anterior'],
    ['dia' => 'Qua', 'data' => '30', 'classe' => 'agendamento-dia-mes-anterior'],
    ['dia' => 'Qui', 'data' => '01'],
    ['dia' => 'Sex', 'data' => '02', 'selecionado' => true],
    ['dia' => 'Sáb', 'data' => '03'],
    ['dia' => 'Dom', 'data' => '04'],
];
$usuarioAtual = usuarioAtual();
$agendamentos = array_map(static function (array $item) use ($usuarioAtual): array {
    $profissional = null;
    $pet = null;
    foreach (profissionaisDisponiveis() as $perfil) { if ((int) $perfil['id'] === (int) $item['profissional_id']) { $profissional = $perfil; break; } }
    foreach (dados('pets') as $candidato) { if ((int) $candidato['id'] === (int) $item['pet_id']) { $pet = $candidato; break; } }
    $pessoa = $profissional['nome'] ?? 'Profissional';
    if ($usuarioAtual['tipo'] === 'profissional') {
        foreach (dados('usuarios') as $candidato) { if ((int) $candidato['id'] === (int) $item['tutor_id']) { $pessoa = $candidato['nome']; break; } }
    }
    $item['pessoa'] = $pessoa;
    $item['iniciais'] = implode('', array_map(static fn(string $parte): string => strtoupper(substr($parte, 0, 1)), array_slice(explode(' ', $pessoa), 0, 2)));
    $item['pet'] = $pet['nome'] ?? 'Pet';
    $item['classe'] = stripos($item['status'], 'confirm') !== false || stripos($item['status'], 'pago') !== false ? 'confirmado' : 'pendente';
    $item['quando'] = dataPorExtenso($item['data']) . ' · ' . $item['horario'];
    $item['valor'] = moeda((float) $item['valor']);
    $item['destino'] = $usuarioAtual['tipo'] === 'tutor' ? 'confirmacao.php?id=' . (int) $item['id'] : 'chatprofissional.php';
    return $item;
}, agendamentosDoUsuario($usuarioAtual));
usort($agendamentos, static fn(array $a, array $b): int => strcmp($a['data'] . $a['horario'], $b['data'] . $b['horario']));
renderizarPagina('agenda.php');
