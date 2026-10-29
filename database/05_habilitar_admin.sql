-- La cuenta administrativa se crea con scripts/crear-admin.ps1.
-- Este archivo solo permite verificar su existencia; no promueve registros públicos.
SELECT nombre, correo, rol FROM usuario_bodega WHERE rol = 'ADMIN';
