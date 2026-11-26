<?php require VIEW_PATH . '/partials/header.php'; ?>
<main id="conteudo" class="pagina-agendamento">
    <div class="booking-container">
        <section class="booking-busca" aria-labelledby="titulo-booking">
            <h1 id="titulo-booking">Tudo pronto para o cuidado?</h1>
            <form class="booking-filtros" method="post" action="servico.php">
                <input type="hidden" name="acao" value="escolher_servico"><?= csrfCampo() ?>
                <input type="hidden" name="profissional_id" value="<?= (int) $profissionalBooking['id'] ?>">
                <label>Serviço
                    <select name="servico"><option><?= escapar($rascunhoBooking['servico']) ?></option></select>
                </label>
                <label>Local
                    <input type="text" name="local" value="<?= escapar($rascunhoBooking['local'] ?? '') ?>" aria-label="Local do atendimento">
                </label>
                <label>Data
                    <input type="text" name="data" value="<?= escapar($rascunhoBooking['data'] . ' · ' . $rascunhoBooking['horario']) ?>" aria-label="Data e horário do atendimento" readonly>
                </label>
                <label>Pet
                    <select name="pet_id"><option value="<?= (int) $petBooking['id'] ?>"><?= escapar($petBooking['nome']) ?></option></select>
                </label>
                <button class="botao botao-primario" type="submit">Editar busca</button>
            </form>
        </section>

        <nav class="booking-etapas" aria-label="Etapas do agendamento">
            <span class="etapa-concluida"><b>✓</b> Serviço</span>
            <span class="etapa-concluida"><b>✓</b> Profissional</span>
            <span class="etapa-concluida"><b>✓</b> Data e horário</span>
            <span class="etapa-atual"><b>4</b> Revisão</span>
        </nav>

        <div class="booking-conteudo booking-data-conteudo booking-revisao-conteudo">
            <div class="booking-revisao-detalhes">
                <section class="revisao-cartao" aria-labelledby="titulo-cuidado-thor">
                    <div class="revisao-cartao-topo"><h2 id="titulo-cuidado-thor">O cuidado de <?= escapar($petBooking['nome']) ?></h2><a href="servico.php">Alterar serviço</a></div>
                    <div class="revisao-pet-e-profissional">
                        <div class="revisao-pet">
                            <span class="revisao-pet-imagem"><img src="<?= escapar(imagemPet($petBooking)) ?>" alt="<?= escapar($petBooking['nome']) ?>"></span>
                            <span><strong><?= escapar($petBooking['nome']) ?></strong><small><?= escapar($petBooking['raca']) ?> · <?= (int) $petBooking['idade'] ?> anos</small><small><?= escapar($rascunhoBooking['servico']) ?></small></span>
                        </div>
                        <div class="revisao-profissional">
                            <span class="booking-profissional-avatar"><?php if (!empty($profissionalBooking['foto'])): ?><img src="<?= escapar($profissionalBooking['foto']) ?>" alt="Foto de <?= escapar($profissionalBooking['nome']) ?>"><?php else: ?><?= escapar(implode('', array_map(static fn(string $parte): string => strtoupper(substr($parte, 0, 1)), array_slice(explode(' ', $profissionalBooking['nome']), 0, 2)))) ?><?php endif; ?></span>
                            <span><strong><?= escapar($profissionalBooking['nome']) ?></strong><small>★ <?= escapar($profissionalBooking['nota'] ?? '4,9') ?> · <?= escapar($profissionalBooking['avaliacoes'] ?? '32 avaliações') ?></small><a href="cuidador.php">Alterar profissional</a></span>
                        </div>
                    </div>
                </section>

                <section class="revisao-cartao" aria-labelledby="titulo-quando-onde">
                    <div class="revisao-cartao-topo"><h2 id="titulo-quando-onde">Quando e onde</h2><a href="data.php">Alterar</a></div>
                    <div class="revisao-linha-detalhe"><?= iconeSvg('calendar') ?><div><strong><?= escapar(dataPorExtenso($rascunhoBooking['data'])) ?></strong><small><?= escapar($rascunhoBooking['horario']) ?>–<?= escapar($horaFim) ?> · 45 minutos</small></div></div>
                    <div class="revisao-linha-detalhe"><?= iconeSvg('pin') ?><div><strong><?= escapar($rascunhoBooking['endereco']) ?></strong><small><?= escapar($rascunhoBooking['local']) ?> · busca e retorno no mesmo local</small></div></div>
                </section>

                <section class="revisao-cartao revisao-orientacoes" aria-labelledby="titulo-orientacoes">
                    <h2 id="titulo-orientacoes">Algum cuidado especial?</h2>
                    <label for="orientacoes-pet">Orientações para o profissional</label>
                    <textarea id="orientacoes-pet" rows="2" readonly><?= escapar($petBooking['observacoes'] ?: 'As orientações podem ser combinadas diretamente pelo chat.') ?></textarea>
                    <p>Você também pode combinar os detalhes pelo chat.</p>
                </section>
                <a class="booking-voltar-servicos" href="data.php">Voltar para data e horário</a>
            </div>

            <aside class="booking-resumo booking-resumo-data" aria-labelledby="titulo-resumo-booking">
                <h2 id="titulo-resumo-booking">Seu agendamento</h2>
                <div class="booking-pet-resumo">
                    <span class="booking-pet-imagem"><img src="<?= escapar(imagemPet($petBooking)) ?>" alt="<?= escapar($petBooking['nome']) ?>"></span>
                    <span><strong><?= escapar($petBooking['nome']) ?></strong><small><?= escapar($petBooking['raca']) ?> · <?= (int) $petBooking['idade'] ?> anos</small></span>
                </div>
                <div class="booking-resumo-servico">
                    <strong><?= escapar($rascunhoBooking['servico']) ?></strong>
                    <p>Um atendimento pensado para a rotina do seu pet.</p>
                    <div><span class="booking-ana-mini"><?= escapar(implode('', array_map(static fn(string $parte): string => strtoupper(substr($parte, 0, 1)), array_slice(explode(' ', $profissionalBooking['nome']), 0, 2)))) ?></span><span><?= escapar($profissionalBooking['nome']) ?><br><small>★ <?= escapar($profissionalBooking['nota'] ?? '4,9') ?> · <?= escapar($profissionalBooking['avaliacoes'] ?? '32 avaliações') ?></small></span></div>
                    <div class="booking-data-linha"><?= iconeSvg('calendar') ?><span><?= escapar(dataPorExtenso($rascunhoBooking['data'], false)) ?><br><small><?= escapar($rascunhoBooking['horario']) ?>–<?= escapar($horaFim) ?></small></span></div>
                    <div><span><?= escapar($rascunhoBooking['servico']) ?></span><strong><?= escapar(moeda((float) ($rascunhoBooking['valor'] ?? 45))) ?></strong></div>
                </div>
                <div class="booking-total"><strong>Total</strong><b><?= escapar(moeda((float) ($rascunhoBooking['valor'] ?? 45))) ?></b></div>
                <form method="post" action="revisao.php"><input type="hidden" name="acao" value="confirmar_agendamento"><?= csrfCampo() ?><button class="botao botao-primario botao-cheio" type="submit">Confirmar agendamento</button></form>
                <p class="booking-observacao">Você poderá revisar os detalhes antes de confirmar.</p>
            </aside>

            <aside class="booking-dica booking-dica-data">
                <?= iconeSvg('heart') ?>
                <div><h2>Um passeio pensado para Thor</h2><p>Compartilhe a rotina e os cuidados do seu pet com o profissional pelo chat.</p></div>
            </aside>
        </div>
    </div>
</main>
<?php require VIEW_PATH . '/partials/footer.php'; ?>
