2Look
App móvil de asesoramiento estético con IA para barberías. La hice como proyecto final del ciclo de DAM en el Instituto Puig Castellar.

¿De qué va?
La idea salió de algo que nos pasa a todos: vas a la barbería, te preguntan qué quieres y dices "lo que tú veas" porque no tienes ni idea de qué corte te queda bien. 2Look intenta resolver eso. Te haces una foto, la IA analiza tu cara y te recomienda los 3 cortes que mejor te van a quedar. Además te enseña cómo te quedaría cada uno sobre tu propia foto, no sobre un modelo aleatorio.

Y de paso puedes reservar cita con un barbero, pagar con Stripe y todo el rollo.

Tecnologías que uso


Frontend

Ionic + Vue 3
Capacitor (para acceso a cámara y generar el APK)
Pinia para el estado
Desplegado en Vercel


Backend

Spring Boot (Java 21)
Spring Data JPA + Hibernate
Spring Security para CORS
Desplegado en Render con Docker


Base de datos

PostgreSQL en Aiven Cloud


IA e imágenes

Gemini (análisis facial)
fal.ai con FLUX.1 Kontext (genera el cambio de peinado sobre tu cara)
Pagos

Stripe (en modo test)
Probarla
La app está desplegada en https://2look-app.vercel.app

Puedes entrar como invitado sin registrarte. Tienes 3 análisis de IA por dispositivo. Para los pagos usa la tarjeta de prueba de Stripe: 4242 4242 4242 4242, fecha cualquiera futura y CVC 123.

Para quién es
Pensada para gente que quiere acertar con su corte sin tener que apostar a ciegas, y también para barberías que quieran ofrecer un extra a sus clientes. Es un proyecto académico pero la base es perfectamente extensible a algo real.

Autor
Iván Martín Bodas — DAM 2025/2026
