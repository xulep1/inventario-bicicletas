# **Diagrama Entidad-Relación (DER) del sistema de inventario de bicicletas**

El Diagrama Entidad-Relación (DER) representa la estructura de la base de datos del sistema de inventario de bicicletas. Su objetivo es organizar la información, identificar las entidades principales y establecer las relaciones que existen entre ellas.

Este modelo se desarrolla teniendo en cuenta la recomendación del profesor, que consiste en relacionar el identificador del código QR con los registros almacenados en la base de datos. De esta manera, cada producto puede identificarse mediante un código único, facilitando su búsqueda y consulta dentro del sistema.

## **1. Descripción de las entidades**

El modelo está compuesto por cuatro entidades principales: Categorías, Productos, Usuarios y Movimientos de inventario.

**Entidad Categorías:** permite organizar los productos según su clasificación dentro de la tienda. Contiene información como el identificador de la categoría, su nombre, descripción y estado de actividad. Entre sus categorías principales se encuentran bicicletas, repuestos y accesorios. Su función es facilitar la organización y búsqueda de los artículos.

**Entidad Productos:** es una de las entidades más importantes del sistema, porque almacena la información de los artículos disponibles en el inventario. Contiene datos como el identificador del producto, código SKU, nombre, descripción, marca, modelo, talla, rin, unidad de medida, precio unitario, stock actual, stock mínimo, código QR, clasificación ABC, categoría y fecha de creación.

Cada producto cuenta con un SKU único que permite identificar su referencia dentro del inventario. Los campos de marca, modelo, talla y rin permiten describir sus características y diferenciar las variantes cuando sea necesario llevar un control de existencias independiente.

**Entidad Usuarios:** almacena la información de las personas encargadas de realizar operaciones en el inventario. Incluye el identificador del usuario, nombres, correo electrónico y rol. Esta entidad permite relacionar los movimientos realizados con las personas responsables de cada operación. Sin embargo, la autenticación de usuarios todavía no se encuentra validada en esta versión del proyecto.

**Entidad Movimientos de inventario:** permite registrar las operaciones que modifican las cantidades disponibles de los productos. Almacena información como el identificador del movimiento, tipo de operación, cantidad, observaciones, fecha, producto relacionado y usuario responsable. Su función principal es conservar un historial de las entradas y salidas del inventario.

## **2. Relaciones entre las entidades**

Las entidades se encuentran conectadas mediante relaciones que permiten compartir información y mantener organizada la base de datos.

La relación entre Categorías y Productos permite que una categoría pueda contener varios productos, mientras que cada producto pertenece a una categoría.

La relación entre Productos y Movimientos de inventario establece que un producto puede tener diferentes movimientos registrados, pero cada movimiento corresponde a un producto específico.

La relación entre Usuarios y Movimientos de inventario permite que un usuario pueda realizar varias operaciones, mientras que cada movimiento queda relacionado con el usuario que lo realizó.

Estas relaciones se establecen mediante claves primarias y claves foráneas, que permiten identificar los registros y mantener la integridad de la información.

## **3. Relación entre el código QR y la base de datos**

Una de las características más importantes del modelo es la incorporación del campo `codigo_qr` en la entidad Productos. Este atributo permite almacenar el identificador interno asociado a cada producto y relacionarlo directamente con su registro en la base de datos.

El identificador utiliza una estructura que contiene el nombre del sistema, el ID del producto y su código SKU. Por ejemplo, puede tener el siguiente formato: PATC-INVENTARIO;PRODUCTO_ID=1;SKU=BIC-MTB-29-M.

Esta estructura permite reconocer el sistema al que pertenece el código, identificar el producto mediante su ID y conocer su referencia comercial a través del SKU.

Cuando se registra un producto, el sistema genera su identificador interno y lo almacena en la base de datos. Posteriormente, se genera una imagen QR que contiene un enlace web hacia la ficha del producto. Cuando el usuario escanea el código, el sistema reconoce el enlace o el identificador interno y realiza la búsqueda correspondiente para recuperar la información almacenada.

Es importante diferenciar el identificador interno del enlace web. El identificador permite reconocer el producto dentro de la base de datos, mientras que el enlace facilita el acceso a su ficha desde el navegador. La dirección generada tiene una estructura similar a `/producto.html?id=1` y depende de la configuración del servidor mediante la variable `APP_BASE_URL`.

## **4. Control de variantes y referencias de productos**

El diseño de la base de datos también considera las características particulares de una tienda de bicicletas. Por ejemplo, una misma bicicleta puede estar disponible en diferentes tallas y tamaños de rin.

Aunque varios productos pertenezcan a la misma marca y modelo, pueden necesitar un control de existencias independiente. Por esta razón, cada referencia o variante que se administre por separado debe tener un código SKU único.

Esto permite diferenciar las bicicletas, repuestos y accesorios, evitando confusiones al momento de consultar las cantidades disponibles o registrar movimientos.

Es importante aclarar que el sistema administra las existencias por referencia o SKU, pero no identifica individualmente cada bicicleta mediante su número de serie físico. Su objetivo es controlar las cantidades de productos disponibles en la tienda.

## **5. Importancia del modelo entidad-relación**

El modelo entidad-relación es importante porque establece una estructura organizada para almacenar y administrar la información del inventario. Además, permite mantener conectados los datos de los productos, las categorías, los usuarios y los movimientos realizados.

Su implementación facilita la identificación de los artículos mediante códigos QR y SKU, mejora el control de las entradas y salidas, conserva el historial de las operaciones y permite consultar las cantidades disponibles.

También contribuye a mantener la integridad de la información mediante el uso de claves primarias y foráneas. Esto reduce la duplicación de registros y facilita el mantenimiento y la incorporación de nuevas funcionalidades al sistema.

## **6. Conclusión**

El Diagrama Entidad-Relación propuesto constituye una parte fundamental del diseño de la base de datos del sistema de inventario de bicicletas, ya que permite organizar la información y establecer las conexiones entre sus principales entidades.

La incorporación del campo `codigo_qr` en la tabla de productos responde a la recomendación del profesor y permite relacionar los códigos QR con los registros almacenados en la base de datos. Esto facilita la identificación de los artículos y el acceso a sus respectivas fichas.

Además, el uso de códigos SKU únicos permite diferenciar las referencias y variantes de los productos, mientras que el registro de movimientos facilita el seguimiento de las existencias.

En conclusión, este modelo proporciona una estructura adecuada para desarrollar un sistema de inventario organizado, que contribuya a mejorar el control de los productos, reducir errores en los registros y facilitar la administración de una tienda de bicicletas.
