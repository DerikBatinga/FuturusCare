<?php
require_once __DIR__ . '/../src/bootstrap.php';

$tituloPagina = 'Profissionais | FuturusCare';
$paginaAtual = 'profissionais.php';
$servicoBuscado = trim((string) ($_GET['servico'] ?? ''));
$profissionais = buscarProfissionaisPorServico($servicoBuscado);
$servicosDisponiveis = [];

foreach (profissionaisDisponiveis() as $profissional) {
    foreach ($profissional['servicos'] ?? [] as $servico) {
        $servicosDisponiveis[$servico] = $servico;
    }
}
sort($servicosDisponiveis, SORT_NATURAL | SORT_FLAG_CASE);

foreach ($profissionais as &$profissional) {
    $partesNome = array_slice(explode(' ', $profissional['nome']), 0, 2);
    $profissional['iniciais'] = implode('', array_map(static fn(string $parte): string => strtoupper(substr($parte, 0, 1)), $partesNome));
    $profissional['nota'] = $profissional['nota'] ?? '4,9';
    $profissional['avaliacoes'] = $profissional['avaliacoes'] ?? '32 avaliações';
    $profissional['menor_preco'] = $profissional['precos'] ? min(array_map('floatval', $profissional['precos'])) : (float) $profissional['preco'];
}
unset($profissional);
renderizarPagina('profissionais.php');
