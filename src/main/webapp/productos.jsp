
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="pe.edu.sabormarino.modelo.Producto"%>

<%
    List<Producto> productos =
            (List<Producto>) request.getAttribute("productos");
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Productos - El Sabor Marino</title>

    <style>
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            padding: 30px;
            font-family: Arial, sans-serif;
            background: #f2f6f7;
            color: #333;
        }

        .contenedor {
            max-width: 1200px;
            margin: auto;
        }

        h1 {
            color: #087e8b;
            text-align: center;
            margin-bottom: 10px;
        }

        .subtitulo {
            text-align: center;
            color: #666;
            margin-bottom: 25px;
        }

        .barra {
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
            gap: 12px;
            margin-bottom: 25px;
        }

        .botones-superiores {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
        }

        .btn-agregar,
        .btn-excel {
            display: inline-block;
            color: white;
            padding: 12px 20px;
            border-radius: 6px;
            text-decoration: none;
            font-weight: bold;
        }

        .btn-agregar {
            background: #087e8b;
        }

        .btn-agregar:hover {
            background: #06636d;
        }

        .btn-excel {
            background: #217346;
        }

        .btn-excel:hover {
            background: #185735;
        }

        .productos {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 25px;
        }

        .tarjeta {
            background: white;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 4px 12px rgba(0,0,0,0.10);
        }

        .tarjeta img {
            width: 100%;
            height: 190px;
            object-fit: cover;
        }

        .contenido {
            padding: 18px;
        }

        .contenido h2 {
            color: #087e8b;
            margin-top: 0;
        }

        .descripcion {
            color: #666;
            min-height: 35px;
        }

        .precio {
            font-size: 21px;
            font-weight: bold;
            color: #e67e22;
        }

        .stock {
            color: #555;
        }

        .acciones {
            display: flex;
            gap: 10px;
            margin-top: 15px;
        }

        .btn-editar,
        .btn-desactivar {
            flex: 1;
            padding: 10px;
            border-radius: 6px;
            text-decoration: none;
            text-align: center;
            color: white;
            border: none;
            cursor: pointer;
            font-size: 14px;
        }

        .btn-editar {
            background: #2878b5;
        }

        .btn-editar:hover {
            background: #1d5d8d;
        }

        .btn-desactivar {
            background: #c0392b;
        }

        .btn-desactivar:hover {
            background: #962d22;
        }

        .mensaje-vacio {
            background: white;
            padding: 30px;
            text-align: center;
            border-radius: 10px;
        }

        .volver {
            display: inline-block;
            margin-top: 25px;
            color: #087e8b;
            text-decoration: none;
            font-weight: bold;
        }

        @media (max-width: 600px) {
            body {
                padding: 15px;
            }

            .barra {
                align-items: flex-start;
                flex-direction: column;
            }

            .botones-superiores {
                width: 100%;
            }
        }
    </style>
</head>

<body>

<div class="contenedor">

    <h1>Menú de Productos</h1>
    <p class="subtitulo">Cevichería El Sabor Marino</p>

    <div class="barra">

        <span>Administración de productos</span>

        <div class="botones-superiores">

            <a href="<%= request.getContextPath() %>/productos?accion=nuevo"
               class="btn-agregar">
                + Registrar producto
            </a>

            <a href="<%= request.getContextPath() %>/reporteProductos"
               class="btn-excel">
                Descargar Excel
            </a>

        </div>

    </div>

    <% if (productos != null && !productos.isEmpty()) { %>

        <div class="productos">

            <% for (Producto producto : productos) {

                String nombre = producto.getNombre().toLowerCase();
                String imagen = "imagenes/ceviche-clasico.jpg";

                if (nombre.contains("arroz")) {
                    imagen = "imagenes/arroz-con-mariscos.jpg";
                } else if (nombre.contains("pescado")) {
                    imagen = "imagenes/pescado-frito.jpg";
                } else if (nombre.contains("ceviche")) {
                    imagen = "imagenes/ceviche-clasico.jpg";
                }
            %>

                <div class="tarjeta">

                    <img src="<%= request.getContextPath() %>/<%= imagen %>"
                         alt="<%= producto.getNombre() %>">

                    <div class="contenido">

                        <h2><%= producto.getNombre() %></h2>

                        <p class="descripcion">
                            <%= producto.getDescripcion() != null
                                    ? producto.getDescripcion()
                                    : "Delicioso plato de nuestra cevichería." %>
                        </p>

                        <p class="precio">
                            S/ <%= String.format(java.util.Locale.US,
                                    "%.2f", producto.getPrecio()) %>
                        </p>

                        <p class="stock">
                            Stock disponible: <%= producto.getStock() %>
                        </p>

                        <div class="acciones">

                            <a class="btn-editar"
                               href="<%= request.getContextPath() %>/productos?accion=editar&id=<%= producto.getIdProducto() %>">
                                Editar
                            </a>

                            <form action="<%= request.getContextPath() %>/productos"
                                  method="POST"
                                  style="flex:1; margin:0;"
                                  onsubmit="return confirm('¿Deseas desactivar este producto?');">

                                <input type="hidden"
                                       name="accion"
                                       value="desactivar">

                                <input type="hidden"
                                       name="idProducto"
                                       value="<%= producto.getIdProducto() %>">

                                <button type="submit"
                                        class="btn-desactivar"
                                        style="width:100%;">
                                    Desactivar
                                </button>

                            </form>

                        </div>

                    </div>
                </div>

            <% } %>

        </div>

    <% } else { %>

        <div class="mensaje-vacio">
            <h2>No hay productos registrados</h2>
            <p>Registra un producto para mostrarlo en el menú.</p>
        </div>

    <% } %>

    <a class="volver"
       href="<%= request.getContextPath() %>/index.html">
        ← Volver al inicio
    </a>

</div>

</body>
</html>
