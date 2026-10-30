
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="pe.edu.sabormarino.modelo.Producto"%>

<%
    Producto producto = (Producto) request.getAttribute("producto");
    boolean editar = producto != null;
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= editar ? "Editar producto" : "Registrar producto" %></title>

    <style>
        * {
            box-sizing: border-box;
        }

        body {
            font-family: Arial, sans-serif;
            background: #f2f6f7;
            margin: 0;
            padding: 30px;
        }

        .contenedor {
            max-width: 550px;
            margin: 30px auto;
            background: white;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.10);
        }

        h1 {
            text-align: center;
            color: #087e8b;
            margin-bottom: 25px;
        }

        label {
            display: block;
            margin-top: 15px;
            font-weight: bold;
            color: #333;
        }

        input, textarea {
            width: 100%;
            padding: 12px;
            margin-top: 7px;
            border: 1px solid #ccc;
            border-radius: 6px;
            font-size: 15px;
        }

        textarea {
            resize: vertical;
            min-height: 90px;
        }

        .botones {
            display: flex;
            gap: 10px;
            margin-top: 25px;
        }

        button, .volver {
            flex: 1;
            padding: 12px;
            border: none;
            border-radius: 6px;
            text-align: center;
            text-decoration: none;
            font-size: 15px;
            cursor: pointer;
        }

        button {
            background: #087e8b;
            color: white;
        }

        .volver {
            background: #777;
            color: white;
        }

        button:hover {
            background: #06636d;
        }
    </style>
</head>

<body>

<div class="contenedor">

    <h1><%= editar ? "Editar producto" : "Registrar producto" %></h1>

    <form action="<%= request.getContextPath() %>/productos" method="POST">

        <% if (editar) { %>
            <input type="hidden" name="accion" value="actualizar">
            <input type="hidden" name="idProducto"
                   value="<%= producto.getIdProducto() %>">
        <% } else { %>
            <input type="hidden" name="accion" value="registrar">
        <% } %>

        <label>Nombre del producto</label>
        <input type="text" name="nombre"
               value="<%= editar ? producto.getNombre() : "" %>"
               required maxlength="100">

        <label>Descripción</label>
        <textarea name="descripcion"><%= editar && producto.getDescripcion() != null
                ? producto.getDescripcion() : "" %></textarea>

        <label>Precio (S/)</label>
        <input type="number" name="precio" step="0.01" min="0.01"
               value="<%= editar ? producto.getPrecio() : "" %>"
               required>

        <label>Stock</label>
        <input type="number" name="stock" min="0"
               value="<%= editar ? producto.getStock() : "" %>"
               required>

        <label>ID de categoría</label>
        <input type="number" name="idCategoria" min="1"
               value="<%= editar && producto.getIdCategoria() != null
                       ? producto.getIdCategoria() : "" %>">

        <div class="botones">
            <button type="submit">
                <%= editar ? "Guardar cambios" : "Registrar producto" %>
            </button>

            <a class="volver"
               href="<%= request.getContextPath() %>/productos">
                Volver
            </a>
        </div>

    </form>

</div>

</body>
</html>
