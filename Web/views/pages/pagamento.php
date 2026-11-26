<?php require VIEW_PATH . '/partials/header.php'; ?>
<main id="conteudo" class="pagina-pagamento">
    <div class="pagamento-container">
        <section class="pagamento-cabecalho">
            <p>AGENDAMENTO · <?= escapar($petPagamento['nome'] ?? 'PET') ?></p>
            <h1>Pagamento</h1>
            <span>Finalize com tranquilidade. Seus dados de pagamento são protegidos.</span>
        </section>

        <div class="pagamento-layout">
            <div class="pagamento-principal">
                <section class="pagamento-cartao" aria-labelledby="titulo-metodo-pagamento">
                    <div class="pagamento-cartao-topo"><div><h2 id="titulo-metodo-pagamento">Forma de pagamento</h2><p>Escolha como deseja pagar por <?= escapar($agendamentoPagamento['servico']) ?>.</p></div><?= iconeSvg('card') ?></div>
                    <div class="pagamento-metodos">
                        <?php foreach ($metodosPagamento as $indice => $metodo): ?>
                            <label class="pagamento-metodo <?= $indice === 0 ? 'pagamento-metodo-selecionado' : '' ?>">
                                <input form="form-pagamento" type="radio" name="metodo-pagamento" value="<?= htmlspecialchars($metodo['id'], ENT_QUOTES, 'UTF-8') ?>" <?= $indice === 0 ? 'checked' : '' ?>>
                                <span class="pagamento-metodo-icone"><?= iconeSvg($metodo['id'] === 'pix' ? 'pix' : 'card') ?></span>
                                <span class="pagamento-metodo-texto"><strong><?= htmlspecialchars($metodo['titulo'], ENT_QUOTES, 'UTF-8') ?></strong><small><?= htmlspecialchars($metodo['descricao'], ENT_QUOTES, 'UTF-8') ?></small></span>
                                <?php if ($metodo['selo']): ?><span class="pagamento-selo"><?= htmlspecialchars($metodo['selo'], ENT_QUOTES, 'UTF-8') ?></span><?php endif; ?>
                            </label>
                        <?php endforeach; ?>
                    </div>
                    <div class="pagamento-pix-info"><?= iconeSvg('check') ?><p>O código Pix será exibido após a confirmação. Você poderá pagar pelo aplicativo do seu banco.</p></div>
                </section>

                <section class="pagamento-cartao pagamento-seguranca" aria-labelledby="titulo-seguranca-pagamento">
                    <span class="pagamento-seguranca-icone"><?= iconeSvg('shield') ?></span>
                    <div><h2 id="titulo-seguranca-pagamento">Seu cuidado, com segurança</h2><p>O pagamento é associado a este agendamento. A profissional recebe após o atendimento ser concluído.</p></div>
                </section>
            </div>

            <aside class="pagamento-resumo" aria-labelledby="titulo-resumo-pagamento">
                <form id="form-pagamento" method="post" action="pagamento.php"><input type="hidden" name="acao" value="pagamento"><input type="hidden" name="agendamento_id" value="<?= (int) $agendamentoPagamento['id'] ?>">
                <?= csrfCampo() ?>
                <h2 id="titulo-resumo-pagamento">Resumo do pedido</h2>
                <div class="pagamento-servico">
                    <span class="pagamento-pet-imagem"><img src="<?= escapar($petPagamento ? imagemPet($petPagamento) : 'assets/img/pet-home-destaque.png') ?>" alt="<?= escapar($petPagamento['nome'] ?? 'Pet') ?>"></span>
                    <div><strong><?= escapar($agendamentoPagamento['servico']) ?></strong><small><?= escapar($petPagamento['nome'] ?? 'Pet') ?> · 45 minutos</small><small><?= escapar($profissionalPagamento['nome'] ?? 'Profissional') ?></small></div>
                </div>
                <div class="pagamento-data"><span>Data e horário</span><strong><?= escapar(dataPorExtenso($agendamentoPagamento['data'], false)) ?> · <?= escapar($agendamentoPagamento['horario']) ?></strong></div>
                <div class="pagamento-valores"><p><span>Serviço</span><strong><?= escapar(moeda((float) $agendamentoPagamento['valor'])) ?></strong></p><p><span>Taxa de serviço</span><strong>R$ 0,00</strong></p></div>
                <div class="pagamento-total"><strong>Total</strong><b><?= escapar(moeda((float) $agendamentoPagamento['valor'])) ?></b></div>
                <button class="botao botao-primario botao-cheio" type="submit">Confirmar pagamento</button>
                <p class="pagamento-nota">Ambiente seguro · Nenhuma cobrança será processada nesta demonstração.</p>
                <a class="pagamento-voltar" href="agenda.php">Voltar aos agendamentos</a></form>
            </aside>
        </div>
    </div>
</main>
<?php require VIEW_PATH . '/partials/footer.php'; ?>
