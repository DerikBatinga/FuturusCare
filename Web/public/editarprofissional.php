<?php
require_once __DIR__ . '/../src/bootstrap.php';
$usuarioPerfil = usuarioAtual();
$perfilAtual = perfilProfissional((int) $usuarioPerfil['id']);
$diasDisponibilidade = ['Segunda' => 'segunda-feira', 'Terça' => 'terça-feira', 'Quarta' => 'quarta-feira', 'Quinta' => 'quinta-feira', 'Sexta' => 'sexta-feira', 'Sábado' => 'sábado', 'Domingo' => 'domingo'];
$tituloPagina = 'Editar perfil profissional | FuturusCare';
$paginaAtual = 'editarprofissional.php';
$servicosEditaveis = [
    ['id' => 'passeio', 'nome' => 'Dog Walking', 'servico' => 'Passeio com cães', 'descricao' => 'Passeio individual para cães', 'preco' => '45', 'unidade' => 'por passeio'],
    ['id' => 'pet-sitting', 'nome' => 'Pet Sitting', 'servico' => 'Pet sitter', 'descricao' => 'Visitas e companhia na casa do pet', 'preco' => '60', 'unidade' => 'por visita'],
    ['id' => 'hospedagem', 'nome' => 'Hospedagem em casa', 'servico' => 'Hospedagem', 'descricao' => 'Estadia com rotina acompanhada', 'preco' => '90', 'unidade' => 'por noite'],
    ['id' => 'creche', 'nome' => 'Creche', 'servico' => 'Creche', 'descricao' => 'Cuidados durante o dia', 'preco' => '85', 'unidade' => 'por dia'],
];
foreach ($servicosEditaveis as &$itemServico) {
    $itemServico['preco'] = (string) ($perfilAtual['precos'][$itemServico['servico']] ?? $itemServico['preco']);
    $itemServico['selecionado'] = in_array($itemServico['servico'], $perfilAtual['servicos'] ?? [], true);
}
unset($itemServico);
renderizarPagina('editarprofissional.php');
