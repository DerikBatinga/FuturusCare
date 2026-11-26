<?php
declare(strict_types=1);

function renderizarPagina(string $arquivo): void
{
    $pagina = basename($arquivo);
    $caminho = VIEW_PATH . '/pages/' . $pagina;
    if (!is_file($caminho)) {
        http_response_code(404);
        exit('Página não encontrada.');
    }

    extract($GLOBALS, EXTR_SKIP);
    require $caminho;
}
