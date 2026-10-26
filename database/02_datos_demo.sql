

BEGIN;

INSERT INTO producto (nombre, marca, categoria)
VALUES
    ('Cristal', 'Cristal', 'Cerveza'),
    ('Pilsen Callao', 'Pilsen Callao', 'Cerveza');

INSERT INTO presentacion (
    producto_id, descripcion, contenido_ml,
    unidades_por_paquete, precio
)
SELECT id, 'Botella 650 ml - caja de 12', 650, 12, 72.00
FROM producto WHERE nombre = 'Cristal';

INSERT INTO presentacion (
    producto_id, descripcion, contenido_ml,
    unidades_por_paquete, precio
)
SELECT id, 'Lata 355 ml - paquete de 6', 355, 6, 24.00
FROM producto WHERE nombre = 'Cristal';

INSERT INTO presentacion (
    producto_id, descripcion, contenido_ml,
    unidades_por_paquete, precio
)
SELECT id, 'Botella 650 ml - caja de 12', 650, 12, 75.00
FROM producto WHERE nombre = 'Pilsen Callao';

INSERT INTO inventario (presentacion_id, zona, cantidad)
SELECT
    pr.id,
    'Arequipa',
    CASE
        WHEN p.nombre = 'Cristal' AND pr.contenido_ml = 650 THEN 0
        WHEN p.nombre = 'Cristal' AND pr.contenido_ml = 355 THEN 20
        ELSE 3
    END
FROM presentacion pr
JOIN producto p ON p.id = pr.producto_id;

COMMIT;

------------------------------------------------------------------