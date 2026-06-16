describe('Flujo Crítico Business Core: Creación de Reporte', () => {
  it('Debe permitir a un usuario registrar un reporte de mascota', () => {
    // 1. Interceptar la llamada a la API para no guardar datos reales durante la prueba (Stubbing E2E)
    cy.intercept('POST', '**/api/reportes', {
      statusCode: 201,
      body: {
        id: 1,
        mascotaId: 10,
        ubicacion: 'Plaza de Puente Alto',
        estado: 'ACTIVE',
        descripcion: 'Encontré un gatito gris',
        tipoReporte: 'ENCONTRADO'
      }
    }).as('crearReporteRequest');

    // 2. Visitar la página web local
    cy.visit('http://localhost:5173'); // Asegúrate de que Vite corra en este puerto

    // 3. Navegar a la sección de reportes
    cy.contains('Reportes').click(); // Busca un botón en tu menú que diga "Reportes"
    cy.contains('Nuevo Reporte').click(); // Busca un botón para crear

    // 4. Llenar el formulario del reporte (ajusta el 'name' de los inputs según tu código de React)
    cy.get('input[name="mascotaId"]').type('10');
    cy.get('input[name="ubicacion"]').type('Plaza de Puente Alto');
    cy.get('select[name="tipoReporte"]').select('ENCONTRADO');
    cy.get('textarea[name="descripcion"]').type('Encontré un gatito gris cerca de los locales comerciales.');

    // 5. Enviar el formulario
    cy.get('button[type="submit"]').click();

    // 6. Verificar que la API fue llamada
    cy.wait('@crearReporteRequest');

    // 7. Aserción visual: Confirmar que la pantalla muestra éxito
    cy.contains('Reporte creado exitosamente').should('be.visible');
  });
});