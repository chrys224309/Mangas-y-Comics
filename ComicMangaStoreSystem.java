import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;

public class ComicMangaStoreSystem extends JFrame {
    // Modelo de datos
    private final List<ComicManga> inventario = new ArrayList<>();
    private final List<Cliente> clientes = new ArrayList<>();
    
    // Componentes de la interfaz
    private JTabbedPane tabbedPane;
    private JTextArea outputArea;
    
    // Constantes
    private static final double MULTA_POR_DIA = 2.50;
    private static final int DIAS_PRESTAMO = 14;

    public ComicMangaStoreSystem() {
        setTitle("Sistema de Gestión de Tienda de Cómics/Mangas");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Configurar interfaz
        configurarInterfaz();
        
        // Datos de prueba
        cargarDatosDePrueba();
    }
    
    private void configurarInterfaz() {
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Crear pestañas
        tabbedPane = new JTabbedPane();
        
        // Pestaña de Inventario
        JPanel inventarioPanel = crearPanelInventario();
        tabbedPane.addTab("Inventario", inventarioPanel);
        
        // Pestaña de Clientes
        JPanel clientesPanel = crearPanelClientes();
        tabbedPane.addTab("Clientes", clientesPanel);
        
        // Pestaña de Préstamos
        JPanel prestamosPanel = crearPanelPrestamos();
        tabbedPane.addTab("Préstamos", prestamosPanel);
        
        // Pestaña de Búsqueda
        JPanel busquedaPanel = crearPanelBusqueda();
        tabbedPane.addTab("Búsqueda", busquedaPanel);
        
        // Área de salida
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        
        // Organizar componentes
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel crearPanelInventario() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Formulario para añadir cómics/mangas
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        
        JLabel lblTitulo = new JLabel("Título:");
        JTextField txtTitulo = new JTextField();
        
        JLabel lblAutor = new JLabel("Autor/Mangaka:");
        JTextField txtAutor = new JTextField();
        
        JLabel lblEditorial = new JLabel("Editorial:");
        JTextField txtEditorial = new JTextField();
        
        JLabel lblISBN = new JLabel("ISBN:");
        JTextField txtISBN = new JTextField();
        
        JButton btnAgregar = new JButton("Agregar al Inventario");
        btnAgregar.addActionListener(e -> {
            agregarComicManga(
                txtTitulo.getText(),
                txtAutor.getText(),
                txtEditorial.getText(),
                txtISBN.getText()
            );
            txtTitulo.setText("");
            txtAutor.setText("");
            txtEditorial.setText("");
            txtISBN.setText("");
        });
        
        JButton btnListar = new JButton("Listar Disponibles");
        btnListar.addActionListener(e -> listarComicsDisponibles());
        
        formPanel.add(lblTitulo);
        formPanel.add(txtTitulo);
        formPanel.add(lblAutor);
        formPanel.add(txtAutor);
        formPanel.add(lblEditorial);
        formPanel.add(txtEditorial);
        formPanel.add(lblISBN);
        formPanel.add(txtISBN);
        formPanel.add(btnAgregar);
        formPanel.add(btnListar);
        
        // Panel para eliminar
        JPanel eliminarPanel = new JPanel(new BorderLayout());
        JLabel lblEliminar = new JLabel("ISBN a eliminar:");
        JTextField txtEliminar = new JTextField();
        JButton btnEliminar = new JButton("Eliminar del Inventario");
        btnEliminar.addActionListener(e -> eliminarComicManga(txtEliminar.getText()));
        
        JPanel eliminarSubPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        eliminarSubPanel.add(lblEliminar);
        eliminarSubPanel.add(txtEliminar);
        
        eliminarPanel.add(eliminarSubPanel, BorderLayout.CENTER);
        eliminarPanel.add(btnEliminar, BorderLayout.SOUTH);
        
        // Organizar paneles
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(eliminarPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelClientes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Formulario para registrar clientes
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        
        JLabel lblId = new JLabel("ID Cliente:");
        JTextField txtId = new JTextField();
        
        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField();
        
        JButton btnRegistrar = new JButton("Registrar Cliente");
        btnRegistrar.addActionListener(e -> {
            registrarCliente(
                txtId.getText(),
                txtNombre.getText()
            );
            txtId.setText("");
            txtNombre.setText("");
        });
        
        JButton btnListar = new JButton("Listar Clientes");
        btnListar.addActionListener(e -> listarClientes());
        
        formPanel.add(lblId);
        formPanel.add(txtId);
        formPanel.add(lblNombre);
        formPanel.add(txtNombre);
        formPanel.add(btnRegistrar);
        formPanel.add(btnListar);
        
        // Panel para eliminar
        JPanel eliminarPanel = new JPanel(new BorderLayout());
        JLabel lblEliminar = new JLabel("ID Cliente a eliminar:");
        JTextField txtEliminar = new JTextField();
        JButton btnEliminar = new JButton("Eliminar Cliente");
        btnEliminar.addActionListener(e -> eliminarCliente(txtEliminar.getText()));
        
        JPanel eliminarSubPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        eliminarSubPanel.add(lblEliminar);
        eliminarSubPanel.add(txtEliminar);
        
        eliminarPanel.add(eliminarSubPanel, BorderLayout.CENTER);
        eliminarPanel.add(btnEliminar, BorderLayout.SOUTH);
        
        // Organizar paneles
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(eliminarPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelPrestamos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel mainPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        
        // Panel de préstamo
        JPanel prestamoPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        
        JLabel lblClientePrestamo = new JLabel("ID Cliente:");
        JTextField txtClientePrestamo = new JTextField();
        
        JLabel lblIsbnPrestamo = new JLabel("ISBN Cómic/Manga:");
        JTextField txtIsbnPrestamo = new JTextField();
        
        JButton btnPrestar = new JButton("Realizar Préstamo");
        btnPrestar.addActionListener(e -> realizarPrestamo(
            txtClientePrestamo.getText(),
            txtIsbnPrestamo.getText()
        ));
        
        prestamoPanel.add(lblClientePrestamo);
        prestamoPanel.add(txtClientePrestamo);
        prestamoPanel.add(lblIsbnPrestamo);
        prestamoPanel.add(txtIsbnPrestamo);
        prestamoPanel.add(btnPrestar);
        
        // Panel de devolución
        JPanel devolucionPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        
        JLabel lblClienteDevolucion = new JLabel("ID Cliente:");
        JTextField txtClienteDevolucion = new JTextField();
        
        JLabel lblIsbnDevolucion = new JLabel("ISBN Cómic/Manga:");
        JTextField txtIsbnDevolucion = new JTextField();
        
        JButton btnDevolver = new JButton("Realizar Devolución");
        btnDevolver.addActionListener(e -> realizarDevolucion(
            txtClienteDevolucion.getText(),
            txtIsbnDevolucion.getText()
        ));
        
        JButton btnVerMultas = new JButton("Ver Multas Pendientes");
        btnVerMultas.addActionListener(e -> listarMultasPendientes());
        
        devolucionPanel.add(lblClienteDevolucion);
        devolucionPanel.add(txtClienteDevolucion);
        devolucionPanel.add(lblIsbnDevolucion);
        devolucionPanel.add(txtIsbnDevolucion);
        devolucionPanel.add(btnDevolver);
        devolucionPanel.add(btnVerMultas);
        
        mainPanel.add(prestamoPanel);
        mainPanel.add(devolucionPanel);
        
        panel.add(mainPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        
        JLabel lblBusqueda = new JLabel("Término de búsqueda:");
        JTextField txtBusqueda = new JTextField();
        
        JLabel lblTipoBusqueda = new JLabel("Buscar por:");
        JComboBox<String> comboBusqueda = new JComboBox<>(new String[]{"Título", "Autor/Mangaka", "ISBN"});
        
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscarComicManga(
            txtBusqueda.getText(),
            comboBusqueda.getSelectedIndex()
        ));
        
        JButton btnListarTodo = new JButton("Listar Todo el Inventario");
        btnListarTodo.addActionListener(e -> listarTodoInventario());
        
        formPanel.add(lblBusqueda);
        formPanel.add(txtBusqueda);
        formPanel.add(lblTipoBusqueda);
        formPanel.add(comboBusqueda);
        formPanel.add(btnBuscar);
        formPanel.add(btnListarTodo);
        
        panel.add(formPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    // Métodos de negocio
    private void agregarComicManga(String titulo, String autor, String editorial, String isbn) {
        if (titulo.isEmpty() || autor.isEmpty() || editorial.isEmpty() || isbn.isEmpty()) {
            outputArea.append("Error: Todos los campos son obligatorios\n");
            return;
        }
        
        if (buscarComicPorISBN(isbn) != null) {
            outputArea.append("Error: Ya existe un cómic/manga con este ISBN\n");
            return;
        }
        
        ComicManga nuevo = new ComicManga(titulo, autor, editorial, isbn);
        inventario.add(nuevo);
        outputArea.append("Cómic/Manga agregado: " + titulo + " (" + isbn + ")\n");
    }
    
    private void eliminarComicManga(String isbn) {
        ComicManga comic = buscarComicPorISBN(isbn);
        if (comic == null) {
            outputArea.append("Error: No se encontró el cómic/manga con ISBN " + isbn + "\n");
            return;
        }
        
        if (comic.getEstado() == EstadoComic.PRESTADO) {
            outputArea.append("Error: No se puede eliminar, está prestado actualmente\n");
            return;
        }
        
        inventario.remove(comic);
        outputArea.append("Cómic/Manga eliminado: " + comic.getTitulo() + " (" + isbn + ")\n");
    }
    
    private void registrarCliente(String id, String nombre) {
        if (id.isEmpty() || nombre.isEmpty()) {
            outputArea.append("Error: ID y nombre son obligatorios\n");
            return;
        }
        
        if (buscarClientePorId(id) != null) {
            outputArea.append("Error: Ya existe un cliente con este ID\n");
            return;
        }
        
        Cliente nuevo = new Cliente(id, nombre);
        clientes.add(nuevo);
        outputArea.append("Cliente registrado: " + nombre + " (" + id + ")\n");
    }
    
    private void eliminarCliente(String id) {
        Cliente cliente = buscarClientePorId(id);
        if (cliente == null) {
            outputArea.append("Error: No se encontró el cliente con ID " + id + "\n");
            return;
        }
        
        if (!cliente.getComicsPrestados().isEmpty()) {
            outputArea.append("Error: El cliente tiene cómics/mangas prestados\n");
            return;
        }
        
        clientes.remove(cliente);
        outputArea.append("Cliente eliminado: " + cliente.getNombre() + " (" + id + ")\n");
    }
    
    private void realizarPrestamo(String idCliente, String isbn) {
        Cliente cliente = buscarClientePorId(idCliente);
        ComicManga comic = buscarComicPorISBN(isbn);
        
        if (cliente == null || comic == null) {
            outputArea.append("Error: Cliente o cómic/manga no encontrado\n");
            return;
        }
        
        if (comic.getEstado() == EstadoComic.PRESTADO) {
            outputArea.append("Error: El cómic/manga ya está prestado\n");
            return;
        }
        
        // Verificar si el cliente ya tiene prestado este cómic
        for (Prestamo prestamo : cliente.getComicsPrestados()) {
            if (prestamo.getComic().getIsbn().equals(isbn)) {
                outputArea.append("Error: El cliente ya tiene prestado este cómic/manga\n");
                return;
            }
        }
        
        comic.setEstado(EstadoComic.PRESTADO);
        Prestamo nuevoPrestamo = new Prestamo(comic, LocalDate.now());
        cliente.agregarPrestamo(nuevoPrestamo);
        
        outputArea.append("Préstamo realizado:\n");
        outputArea.append("Cliente: " + cliente.getNombre() + "\n");
        outputArea.append("Cómic/Manga: " + comic.getTitulo() + "\n");
        outputArea.append("Fecha de préstamo: " + nuevoPrestamo.getFechaPrestamo() + "\n");
        outputArea.append("Fecha de devolución esperada: " + 
            nuevoPrestamo.getFechaPrestamo().plusDays(DIAS_PRESTAMO) + "\n");
    }
    
    private void realizarDevolucion(String idCliente, String isbn) {
        Cliente cliente = buscarClientePorId(idCliente);
        ComicManga comic = buscarComicPorISBN(isbn);
        
        if (cliente == null || comic == null) {
            outputArea.append("Error: Cliente o cómic/manga no encontrado\n");
            return;
        }
        
        Prestamo prestamo = null;
        for (Prestamo p : cliente.getComicsPrestados()) {
            if (p.getComic().getIsbn().equals(isbn)) {
                prestamo = p;
                break;
            }
        }
        
        if (prestamo == null) {
            outputArea.append("Error: Este cliente no tiene prestado este cómic/manga\n");
            return;
        }
        
        comic.setEstado(EstadoComic.DISPONIBLE);
        cliente.eliminarPrestamo(prestamo);
        
        LocalDate fechaDevolucion = LocalDate.now();
        long diasAtraso = ChronoUnit.DAYS.between(
            prestamo.getFechaPrestamo().plusDays(DIAS_PRESTAMO), 
            fechaDevolucion
        );
        
        double multa = 0;
        if (diasAtraso > 0) {
            multa = diasAtraso * MULTA_POR_DIA;
            cliente.agregarMulta(multa);
            outputArea.append("¡Devolución tardía! Multa aplicada: $" + String.format("%.2f", multa) + "\n");
        }
        
        outputArea.append("Devolución realizada:\n");
        outputArea.append("Cliente: " + cliente.getNombre() + "\n");
        outputArea.append("Cómic/Manga: " + comic.getTitulo() + "\n");
        outputArea.append("Fecha de devolución: " + fechaDevolucion + "\n");
        if (multa > 0) {
            outputArea.append("Días de atraso: " + diasAtraso + "\n");
        }
    }
    
    private void buscarComicManga(String termino, int tipo) {
        if (termino.isEmpty()) {
            outputArea.append("Error: Ingrese un término de búsqueda\n");
            return;
        }
        
        List<ComicManga> resultados = new ArrayList<>();
        
        for (ComicManga comic : inventario) {
            boolean coincide = false;
            
            switch (tipo) {
                case 0 -> // Título
                    coincide = comic.getTitulo().toLowerCase().contains(termino.toLowerCase());
                case 1 -> // Autor/Mangaka
                    coincide = comic.getAutor().toLowerCase().contains(termino.toLowerCase());
                case 2 -> // ISBN
                    coincide = comic.getIsbn().equalsIgnoreCase(termino);
            }
            
            if (coincide) {
                resultados.add(comic);
            }
        }
        
        if (resultados.isEmpty()) {
            outputArea.append("No se encontraron resultados\n");
        } else {
            outputArea.append("Resultados de búsqueda (" + resultados.size() + "):\n");
            for (ComicManga comic : resultados) {
                outputArea.append("- " + comic + "\n");
            }
        }
    }
    
    private void listarComicsDisponibles() {
        List<ComicManga> disponibles = new ArrayList<>();
        
        for (ComicManga comic : inventario) {
            if (comic.getEstado() == EstadoComic.DISPONIBLE) {
                disponibles.add(comic);
            }
        }
        
        if (disponibles.isEmpty()) {
            outputArea.append("No hay cómics/mangas disponibles\n");
        } else {
            outputArea.append("Cómics/Mangas disponibles (" + disponibles.size() + "):\n");
            for (ComicManga comic : disponibles) {
                outputArea.append("- " + comic + "\n");
            }
        }
    }
    
    private void listarTodoInventario() {
        if (inventario.isEmpty()) {
            outputArea.append("El inventario está vacío\n");
        } else {
            outputArea.append("Inventario completo (" + inventario.size() + "):\n");
            for (ComicManga comic : inventario) {
                outputArea.append("- " + comic + " (" + comic.getEstado() + ")\n");
            }
        }
    }
    
    private void listarClientes() {
        if (clientes.isEmpty()) {
            outputArea.append("No hay clientes registrados\n");
        } else {
            outputArea.append("Clientes registrados (" + clientes.size() + "):\n");
            for (Cliente cliente : clientes) {
                outputArea.append("- " + cliente + "\n");
                if (!cliente.getComicsPrestados().isEmpty()) {
                    outputArea.append("  Cómics prestados: " + cliente.getComicsPrestados().size() + "\n");
                }
            }
        }
    }
    
    private void listarMultasPendientes() {
        boolean hayMultas = false;
        
        for (Cliente cliente : clientes) {
            if (cliente.getMultaPendiente() > 0) {
                outputArea.append("Cliente: " + cliente.getNombre() + 
                    " - Multa pendiente: $" + String.format("%.2f", cliente.getMultaPendiente()) + "\n");
                hayMultas = true;
            }
        }
        
        if (!hayMultas) {
            outputArea.append("No hay multas pendientes\n");
        }
    }
    
    // Métodos auxiliares de búsqueda
    private ComicManga buscarComicPorISBN(String isbn) {
        for (ComicManga comic : inventario) {
            if (comic.getIsbn().equals(isbn)) {
                return comic;
            }
        }
        return null;
    }
    
    private Cliente buscarClientePorId(String id) {
        for (Cliente cliente : clientes) {
            if (cliente.getId().equals(id)) {
                return cliente;
            }
        }
        return null;
    }
    
    // Datos de prueba
    private void cargarDatosDePrueba() {
        inventario.add(new ComicManga("Watchmen", "Alan Moore", "DC Comics", "978-0930289232"));
        inventario.add(new ComicManga("Batman: The Dark Knight Returns", "Frank Miller", "DC Comics", "978-1563893421"));
        inventario.add(new ComicManga("One Piece", "Eiichiro Oda", "Shueisha", "978-1569319017"));
        inventario.add(new ComicManga("Naruto Vol", "Masashi Kishimoto", "Shueisha", "978-1569319000"));
        inventario.add(new ComicManga("Saga Vol", "Brian K. Vaughan", "Image Comics", "978-1607066019"));
        
        clientes.add(new Cliente("CLI001", "Juan Pérez"));
        clientes.add(new Cliente("CLI002", "María García"));
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ComicMangaStoreSystem sistema = new ComicMangaStoreSystem();
            sistema.setVisible(true);
        });
    }
}

// Enumeración para el estado del cómic/manga
enum EstadoComic {
    DISPONIBLE("Disponible"),
    PRESTADO("Prestado");
    
    private final String descripcion;
    
    EstadoComic(String descripcion) {
        this.descripcion = descripcion;
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
}

// Clase para representar cómics/mangas
class ComicManga {
    private final String titulo;
    private final String autor;
    private final String editorial;
    private final String isbn;
    private EstadoComic estado;
    
    public ComicManga(String titulo, String autor, String editorial, String isbn) {
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.isbn = isbn;
        this.estado = EstadoComic.DISPONIBLE;
    }
    
    // Getters y setters
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getEditorial() { return editorial; }
    public String getIsbn() { return isbn; }
    public EstadoComic getEstado() { return estado; }
    public void setEstado(EstadoComic estado) { this.estado = estado; }
    
    @Override
    public String toString() {
        return titulo + " por " + autor + " (" + editorial + ") - ISBN: " + isbn;
    }
}

// Clase para representar préstamos
class Prestamo {
    private final ComicManga comic;
    private final LocalDate fechaPrestamo;
    
    public Prestamo(ComicManga comic, LocalDate fechaPrestamo) {
        this.comic = comic;
        this.fechaPrestamo = fechaPrestamo;
    }
    
    // Getters
    public ComicManga getComic() { return comic; }
    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
}

// Clase para representar clientes
class Cliente {
    private final String id;
    private final String nombre;
    private final List<Prestamo> comicsPrestados;
    private double multaPendiente;
    
    public Cliente(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.comicsPrestados = new ArrayList<>();
        this.multaPendiente = 0;
    }
    
    // Métodos para gestionar préstamos
    public void agregarPrestamo(Prestamo prestamo) {
        comicsPrestados.add(prestamo);
    }
    
    public void eliminarPrestamo(Prestamo prestamo) {
        comicsPrestados.remove(prestamo);
    }
    
    // Métodos para gestionar multas
    public void agregarMulta(double cantidad) {
        multaPendiente += cantidad;
    }
    
    public void pagarMulta(double cantidad) {
        multaPendiente = Math.max(0, multaPendiente - cantidad);
    }
    
    // Getters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public List<Prestamo> getComicsPrestados() { return comicsPrestados; }
    public double getMultaPendiente() { return multaPendiente; }
    
    @Override
    public String toString() {
        return nombre + " (" + id + ")";
    }
}