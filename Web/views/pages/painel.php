<?php require VIEW_PATH . '/partials/header.php'; ?>
<main id="conteudo" class="pagina-painel-profissional">
    <div class="painel-profissional-container">
        <section class="painel-profissional-boas-vindas">
            <div>
                <p class="painel-profissional-etiqueta">ÁREA PROFISSIONAL</p>
                <h1>Olá, <?= escapar(explode(' ', $usuarioPainel['nome'])[0]) ?>. Vamos cuidar?</h1>
                <p>Veja sua agenda, atualize seus serviços e acompanhe como seu perfil está aparecendo para tutores.</p>
            </div>
            <div class="painel-profissional-arte" aria-hidden="true">
                <img class="painel-profissional-forma" src="assets/img/forma-laranja.svg" alt="">
                <img class="painel-profissional-lilas" src="assets/img/forma-lavanda.svg" alt="">
                <img class="painel-profissional-pet" src="assets/img/composicao-companhia.webp" alt="Cão e gato juntos">
            </div>
        </section>

        <section class="painel-profissional-indicadores" aria-label="Resumo do perfil">
                    <article><span class="painel-indicador-icone"><?= iconeSvg('calendar') ?></span><div><small>Atendimentos cadastrados</small><strong><?= count($atendimentos) ?></strong><p>Atualizado pela sua agenda</p></div></article>
            <article><span class="painel-indicador-icone painel-indicador-amarelo"><?= iconeSvg('star') ?></span><div><small>Avaliação média</small><strong>4,9 <span>★</span></strong><p>Com base em 24 avaliações</p></div></article>
            <article><span class="painel-indicador-icone painel-indicador-pessego"><?= iconeSvg('heart') ?></span><div><small>Seu perfil</small><strong>Completo</strong><p>Visível para tutores da região</p></div></article>
        </section>

        <div class="painel-profissional-grade">
            <div class="painel-profissional-principal">
                <section class="painel-profissional-secao" aria-labelledby="titulo-agenda-profissional">
                    <header class="painel-secao-cabecalho"><div><p class="painel-profissional-etiqueta">QUINTA-FEIRA · 01 DE OUTUBRO</p><h2 id="titulo-agenda-profissional">Sua agenda de hoje</h2></div><a href="agenda.php">Ver agenda completa →</a></header>
                    <div class="painel-agenda-lista">
                        <?php foreach ($atendimentos as $atendimento): ?>
                            <article class="painel-agenda-item">
                                <time><?= htmlspecialchars($atendimento['hora'], ENT_QUOTES, 'UTF-8') ?></time>
                                <span class="painel-agenda-avatar" aria-hidden="true">🐾</span>
                                <div class="painel-agenda-info"><h3><?= htmlspecialchars($atendimento['servico'], ENT_QUOTES, 'UTF-8') ?></h3><p><?= htmlspecialchars($atendimento['pet'], ENT_QUOTES, 'UTF-8') ?> · <?= htmlspecialchars($atendimento['tutor'], ENT_QUOTES, 'UTF-8') ?></p></div>
                                <span class="painel-status-atendimento"><?= htmlspecialchars($atendimento['status'], ENT_QUOTES, 'UTF-8') ?></span>
                            </article>
                        <?php endforeach; ?>
                        <?php if (!$atendimentos): ?><p class="painel-agenda-vazia">Seus próximos atendimentos aparecerão aqui.</p><?php endif; ?>
                    </div>
                </section>

                <section class="painel-profissional-secao" id="servicos-profissional" aria-labelledby="titulo-servicos-profissional">
                    <header class="painel-secao-cabecalho"><div><p class="painel-profissional-etiqueta">O QUE VOCÊ OFERECE</p><h2 id="titulo-servicos-profissional">Seus serviços</h2></div><a class="botao botao-secundario" href="editarprofissional.php#servicos-editaveis">+ Adicionar serviço</a></header>
                    <div class="painel-servicos-lista">
                        <?php foreach ($servicos as $servico): ?>
                            <article class="painel-servico-item"><span class="painel-servico-icone"><?= iconeSvg('paw') ?></span><div><h3><?= htmlspecialchars($servico['nome'], ENT_QUOTES, 'UTF-8') ?></h3><p><?= htmlspecialchars($servico['descricao'], ENT_QUOTES, 'UTF-8') ?></p></div><div class="painel-servico-preco"><strong><?= htmlspecialchars($servico['preco'], ENT_QUOTES, 'UTF-8') ?></strong><small><?= htmlspecialchars($servico['unidade'], ENT_QUOTES, 'UTF-8') ?></small></div><a class="painel-editar-servico" href="editarprofissional.php#servicos-editaveis" aria-label="Editar <?= htmlspecialchars($servico['nome'], ENT_QUOTES, 'UTF-8') ?>">Editar</a></article>
                        <?php endforeach; ?>
                    </div>
                </section>
            </div>

            <aside class="painel-profissional-lateral">
                <section class="painel-perfil-card">
                    <div class="painel-perfil-topo"><?php if (imagemPerfil($usuarioPainel)): ?><img class="painel-avatar-ana painel-avatar-foto" src="<?= escapar(imagemPerfil($usuarioPainel)) ?>" alt="Foto de <?= escapar($usuarioPainel['nome']) ?>"><?php else: ?><span class="painel-avatar-ana"><?= escapar(implode('', array_map(static fn(string $parte): string => strtoupper(substr($parte, 0, 1)), array_slice(explode(' ', $usuarioPainel['nome']), 0, 2)))) ?></span><?php endif; ?><span class="painel-perfil-verificado">✓ Verificado</span></div>
                    <p class="painel-profissional-etiqueta">SEU PERFIL PÚBLICO</p><h2><?= escapar($usuarioPainel['nome']) ?></h2><p class="painel-perfil-local"><?= iconeSvg('pin') ?> <?= escapar($perfilPainel['regiao'] ?? '') ?></p>
                    <div class="painel-perfil-avaliacao"><strong>4,9</strong><span>★★★★★</span><small>24 avaliações</small></div>
                    <div class="painel-perfil-visitas"><span>Visualizações esta semana</span><strong>126</strong></div>
                    <a class="botao botao-contorno botao-cheio" href="profissionalpublico.php">Visualizar perfil</a>
                    <a class="painel-editar-perfil" href="editarprofissional.php">Editar dados do perfil</a>
                </section>
                <section class="painel-dica-card"><?= iconeSvg('sparkle') ?><h2>Uma dica para você</h2><p>Adicionar novas fotos pode ajudar tutores a conhecer melhor seu espaço e seu jeito de cuidar.</p><a href="editarprofissional.php" class="painel-dica-link">Atualizar meu perfil →</a></section>
                <a class="painel-mensagens-link" href="chatprofissional.php"><span>▤</span><span><strong>Mensagens</strong><small>Converse com os tutores</small></span><b>→</b></a>
            </aside>
        </div>

        <section class="painel-dados-perfil" id="dados-profissional" aria-labelledby="titulo-dados-profissional">
            <div><p class="painel-profissional-etiqueta">SEUS DADOS</p><h2 id="titulo-dados-profissional">Informações do perfil</h2><p>Revise os dados que os tutores veem ao conhecer seu trabalho.</p></div>
            <a class="botao botao-secundario" href="editarprofissional.php">Editar informações</a>
        </section>
    </div>
</main>
<?php require VIEW_PATH . '/partials/footer.php'; ?>
