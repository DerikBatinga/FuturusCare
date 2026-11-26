<?php require VIEW_PATH . '/partials/header.php'; ?>
<main id="conteudo" class="pagina-editar-perfil">
    <div class="editar-perfil-container">
        <a class="editar-perfil-voltar" href="tutorpainel.php">← Voltar ao meu perfil</a>
        <section class="editar-perfil-card" aria-labelledby="titulo-editar-perfil">
            <p class="editar-perfil-etiqueta">PERFIL DO TUTOR</p>
            <h1 id="titulo-editar-perfil">Editar perfil</h1>
            <form class="editar-perfil-formulario" method="post" action="edicao.php" enctype="multipart/form-data">
                <input type="hidden" name="acao" value="editar_perfil">
                <?= csrfCampo() ?>
                <label class="editar-perfil-foto" for="foto-perfil">
                    <?php if (imagemPerfil($usuarioPerfil)): ?><img src="<?= escapar(imagemPerfil($usuarioPerfil)) ?>" alt="Foto atual de <?= escapar($usuarioPerfil['nome']) ?>"><?php endif; ?>
                    <svg aria-hidden="true" viewBox="0 0 24 24"><path d="M4 7.5h3l1.4-2h7.2l1.4 2h3A1.5 1.5 0 0 1 21.5 9v9a1.5 1.5 0 0 1-1.5 1.5H4A1.5 1.5 0 0 1 2.5 18V9A1.5 1.5 0 0 1 4 7.5Z"/><circle cx="12" cy="13" r="3.5"/></svg>
                    <strong><?= imagemPerfil($usuarioPerfil) ? 'Trocar foto de perfil' : 'Adicionar foto de perfil' ?></strong>
                    <input id="foto-perfil" name="foto" type="file" accept="image/jpeg,image/png,image/webp" aria-label="Adicionar foto de perfil">
                </label>
                <label class="editar-perfil-campo editar-perfil-nome">Nome<input type="text" name="nome" value="<?= escapar($usuarioPerfil['nome']) ?>" autocomplete="name" required></label>
                <div class="editar-perfil-campos-duplos">
                    <label class="editar-perfil-campo">Telefone<input type="tel" name="telefone" value="<?= escapar($usuarioPerfil['telefone'] ?? '') ?>" autocomplete="tel"></label>
                    <label class="editar-perfil-campo">E-mail<input type="email" name="email" value="<?= escapar($usuarioPerfil['email']) ?>" autocomplete="email" required></label>
                    <label class="editar-perfil-campo">Endereço<input type="text" name="endereco" value="<?= escapar($usuarioPerfil['endereco'] ?? '') ?>" autocomplete="street-address"></label>
                    <label class="editar-perfil-campo">Senha<input type="password" name="senha" placeholder="Deixe em branco para manter" autocomplete="new-password"></label>
                </div>
                <button class="botao botao-primario editar-perfil-salvar" type="submit">Salvar alterações</button>
            </form>
        </section>
    </div>
</main>
<?php require VIEW_PATH . '/partials/footer.php'; ?>
