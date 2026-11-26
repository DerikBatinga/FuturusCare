<?php require VIEW_PATH . '/partials/header.php'; ?>
<main id="conteudo" class="pagina-editar-profissional">
    <div class="editar-profissional-container">
        <a class="editar-profissional-voltar" href="painel.php">← Voltar ao painel</a>
        <header class="editar-profissional-cabecalho">
            <div><p class="editar-profissional-etiqueta">ÁREA PROFISSIONAL</p><h1>Editar seu perfil</h1><p>Mantenha suas informações e serviços atualizados para os tutores.</p></div>
            <a class="botao botao-contorno" href="profissionalpublico.php">Visualizar perfil público</a>
        </header>

        <div class="editar-profissional-layout">
            <form class="editar-profissional-formulario" method="post" action="editarprofissional.php" enctype="multipart/form-data">
                <input type="hidden" name="acao" value="editar_perfil_profissional">
                <?= csrfCampo() ?>
                <section class="editar-profissional-secao" aria-labelledby="titulo-dados-profissionais">
                    <div class="editar-profissional-secao-titulo"><span>01</span><div><h2 id="titulo-dados-profissionais">Seus dados</h2><p>Informações que aparecem no seu perfil público.</p></div></div>
                    <div class="editar-profissional-foto-linha"><?php if (imagemPerfil($usuarioPerfil)): ?><img class="editar-profissional-avatar editar-profissional-avatar-foto" src="<?= escapar(imagemPerfil($usuarioPerfil)) ?>" alt="Foto de <?= escapar($usuarioPerfil['nome']) ?>"><?php else: ?><span class="editar-profissional-avatar"><?= escapar(implode('', array_map(static fn(string $parte): string => strtoupper(substr($parte, 0, 1)), array_slice(explode(' ', $usuarioPerfil['nome']), 0, 2)))) ?></span><?php endif; ?><div><strong>Foto de perfil</strong><p>Uma foto clara ajuda os tutores a reconhecer você.</p></div><label class="editar-profissional-upload">Alterar foto<input type="file" name="foto" accept="image/jpeg,image/png,image/webp"></label></div>
                    <div class="editar-profissional-campos">
                        <label class="campo">Nome completo<input type="text" name="nome" value="<?= escapar($usuarioPerfil['nome']) ?>" autocomplete="name" required></label>
                        <label class="campo">E-mail<input type="email" name="email" value="<?= escapar($usuarioPerfil['email']) ?>" autocomplete="email" required></label>
                        <label class="campo">Celular<input type="tel" name="telefone" value="<?= escapar($usuarioPerfil['telefone'] ?? '') ?>" autocomplete="tel"></label>
                        <label class="campo">Cidade e região atendida<input type="text" name="regiao" value="<?= escapar($perfilAtual['regiao'] ?? '') ?>" required></label>
                        <label class="campo editar-profissional-campo-largo">Sobre você<textarea name="sobre" rows="4"><?= escapar($perfilAtual['sobre'] ?? '') ?></textarea></label>
                    </div>
                </section>

                <section class="editar-profissional-secao" id="servicos-editaveis" aria-labelledby="titulo-servicos-editaveis">
                    <div class="editar-profissional-secao-titulo"><span>02</span><div><h2 id="titulo-servicos-editaveis">Seus serviços</h2><p>Escolha o que oferece e informe seu preço.</p></div></div>
                    <div class="editar-profissional-servicos">
                        <?php foreach ($servicosEditaveis as $index => $servico): ?>
                            <article class="editar-profissional-servico">
                                <label class="editar-profissional-servico-nome"><input type="checkbox" name="servicos[]" value="<?= htmlspecialchars($servico['id'], ENT_QUOTES, 'UTF-8') ?>" <?= $servico['selecionado'] ? 'checked' : '' ?>><span><strong><?= htmlspecialchars($servico['nome'], ENT_QUOTES, 'UTF-8') ?></strong><small><?= htmlspecialchars($servico['descricao'], ENT_QUOTES, 'UTF-8') ?></small></span></label>
                                <label class="editar-profissional-preco">R$ <input type="number" name="preco_<?= htmlspecialchars($servico['id'], ENT_QUOTES, 'UTF-8') ?>" value="<?= htmlspecialchars($servico['preco'], ENT_QUOTES, 'UTF-8') ?>" min="0" step="5"><small><?= htmlspecialchars($servico['unidade'], ENT_QUOTES, 'UTF-8') ?></small></label>
                            </article>
                        <?php endforeach; ?>
                    </div>
                </section>

                <section class="editar-profissional-secao">
                    <div class="editar-profissional-secao-titulo"><span>03</span><div><h2>Disponibilidade</h2><p>Marque os dias em que costuma atender.</p></div></div>
                    <div class="editar-profissional-dias">
                        <?php foreach ($diasDisponibilidade as $dia => $valorDia): ?>
                            <label><input type="checkbox" name="dias[]" value="<?= escapar($valorDia) ?>" <?= in_array($valorDia, $perfilAtual['dias'] ?? [], true) ? 'checked' : '' ?>><span><?= escapar($dia) ?></span></label>
                        <?php endforeach; ?>
                    </div>
                </section>

                <div class="editar-profissional-acoes"><a href="painel.php">Cancelar</a><button class="botao botao-primario" type="submit">Salvar alterações</button></div>
            </form>

            <aside class="editar-profissional-lateral">
                <section class="editar-profissional-previa">
                    <p class="editar-profissional-etiqueta">PRÉVIA DO PERFIL</p>
                    <div class="editar-profissional-previa-foto"><img src="assets/img/forma-laranja.svg" alt=""><?php if (imagemPerfil($usuarioPerfil)): ?><img class="editar-profissional-previa-usuario" src="<?= escapar(imagemPerfil($usuarioPerfil)) ?>" alt="Foto de <?= escapar($usuarioPerfil['nome']) ?>"><?php else: ?><img src="assets/img/composicao-alegria.webp" alt="Cachorro alegre"><?php endif; ?></div>
                    <span class="editar-profissional-verificado"><?= iconeSvg('check') ?> Perfil verificado</span><h2><?= escapar($usuarioPerfil['nome']) ?></h2><p><?= iconeSvg('pin') ?> <?= escapar($perfilAtual['regiao']) ?></p>
                    <div class="editar-profissional-nota"><strong>4,9</strong><span>★★★★★</span><small>24 avaliações</small></div>
                    <a class="botao botao-secundario botao-cheio" href="profissionalpublico.php">Abrir perfil completo</a>
                </section>
                <section class="editar-profissional-dica"><span><?= iconeSvg('sparkle') ?></span><h2>Um perfil completo inspira confiança</h2><p>Conte sobre sua experiência e mantenha os valores dos serviços atualizados para ajudar os tutores a escolher.</p></section>
            </aside>
        </div>
    </div>
</main>
<?php require VIEW_PATH . '/partials/footer.php'; ?>
