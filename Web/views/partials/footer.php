<?php
$linksRodape = [
    'index.php' => 'Início',
    'sobre.php' => 'Sobre nós',
    'servicos.php' => 'Serviços',
    'contato.php' => 'Contato',
];
?>
<footer class="rodape">
    <div class="container rodape-colunas">
        <div class="rodape-marca">
            <a href="index.php"><img src="assets/img/logo-branco.png" alt="FuturusCare" width="200" height="51"></a>
            <!-- Os links sociais serão adicionados quando os endereços forem definidos. -->
            <div class="redes-sociais" role="group" aria-label="Redes sociais — links em breve">
                <svg role="img" aria-label="X" viewBox="0 0 24 24"><path d="M4 3h5l11 18h-5L4 3Zm0 18L20 3"/></svg>
                <svg role="img" aria-label="Instagram" viewBox="0 0 24 24"><rect x="3" y="3" width="18" height="18" rx="5"/><circle cx="12" cy="12" r="4"/><circle cx="17.5" cy="6.5" r=".7"/></svg>
                <svg role="img" aria-label="YouTube" viewBox="0 0 24 24"><rect x="2" y="5" width="20" height="14" rx="4"/><path d="m10 9 5 3-5 3Z"/></svg>
                <svg role="img" aria-label="LinkedIn" viewBox="0 0 24 24"><rect x="3" y="3" width="18" height="18" rx="2"/><path d="M7 10v7m4 0v-7m0 3c0-4 6-4 6 0v4"/><circle cx="7" cy="7" r=".6"/></svg>
            </div>
            <p>Cuidamos do seu pet com todo amor e carinho que ele merece. Serviços profissionais de pet sitting em São Paulo e região.</p>
            <a href="mailto:contato@futuruscare.com.br">contato@futuruscare.com.br</a>
            <a href="tel:+5511999999999">(11) 99999-9999</a>
        </div>
        <nav aria-label="Links rápidos">
            <h2>Links rápidos</h2>
            <ul>
                <?php foreach ($linksRodape as $arquivo => $rotulo): ?>
                    <li>
                        <?php if (is_file(__DIR__ . '/../' . $arquivo)): ?>
                            <a href="<?= $arquivo ?>"><?= $rotulo ?></a>
                        <?php elseif (($paginaAtual ?? '') === 'index.php' && $arquivo === 'sobre.php'): ?>
                            <a href="#sobre"><?= $rotulo ?></a>
                        <?php elseif (($paginaAtual ?? '') === 'index.php' && $arquivo === 'servicos.php'): ?>
                            <a href="#servicos"><?= $rotulo ?></a>
                        <?php else: ?>
                            <span aria-disabled="true"><?= $rotulo ?></span>
                        <?php endif; ?>
                    </li>
                <?php endforeach; ?>
            </ul>
        </nav>
        <div>
            <h2>Serviços</h2>
            <ul>
                <li>Pet Sitting Diário</li>
                <li>Hospedagem em Casa</li>
                <li>Passeios</li>
                <li>Cuidados Especiais</li>
            </ul>
        </div>
    </div>
    <div class="container rodape-creditos">
        <p>© <?= date('Y') ?> FuturusCare. Todos os direitos reservados. Feito com <span aria-label="amor">♥</span> para você e seu pet.</p>
    </div>
</footer>
<script src="assets/js/app.js" defer></script>
</body>
</html>
