-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 24-06-2026 a las 03:52:08
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `mistery_1`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `comentarios`
--

CREATE TABLE `comentarios` (
  `id` int(11) NOT NULL,
  `publicacion_id` int(11) NOT NULL,
  `usuario_id` int(11) NOT NULL,
  `comentario` text NOT NULL,
  `fecha_comentario` timestamp NOT NULL DEFAULT current_timestamp(),
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `failed_jobs`
--

CREATE TABLE `failed_jobs` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `uuid` varchar(255) NOT NULL,
  `connection` text NOT NULL,
  `queue` text NOT NULL,
  `payload` longtext NOT NULL,
  `exception` longtext NOT NULL,
  `failed_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `likes`
--

CREATE TABLE `likes` (
  `id` int(10) UNSIGNED NOT NULL,
  `usuario_id` int(11) NOT NULL,
  `publicacion_id` int(11) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `likes`
--

INSERT INTO `likes` (`id`, `usuario_id`, `publicacion_id`, `created_at`, `updated_at`) VALUES
(15, 1, 2, '2026-06-23 04:16:23', '2026-06-23 04:16:23'),
(16, 1, 1, '2026-06-23 04:16:49', '2026-06-23 04:16:49'),
(19, 1, 3, '2026-06-23 06:46:24', '2026-06-23 06:46:24'),
(20, 1, 4, '2026-06-23 11:35:03', '2026-06-23 11:35:03'),
(21, 1, 7, '2026-06-23 22:53:39', '2026-06-23 22:53:39'),
(23, 2, 9, '2026-06-24 06:36:47', '2026-06-24 06:36:47');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `migrations`
--

CREATE TABLE `migrations` (
  `id` int(10) UNSIGNED NOT NULL,
  `migration` varchar(255) NOT NULL,
  `batch` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `migrations`
--

INSERT INTO `migrations` (`id`, `migration`, `batch`) VALUES
(1, '2014_10_12_000000_create_users_table', 1),
(2, '2014_10_12_100000_create_password_resets_table', 1),
(3, '2019_08_19_000000_create_failed_jobs_table', 1),
(4, '2019_12_14_000001_create_personal_access_tokens_table', 1),
(5, '2026_06_22_214642_add_titulo_to_misterios_table', 2),
(6, '2026_06_23_151607_create_traducciones_table', 3),
(7, '2026_06_23_220228_add_firebase_uid_to_usuarios_table', 4),
(8, '2026_06_23_add_firebase_uid_to_users', 4);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `misterios`
--

CREATE TABLE `misterios` (
  `id` int(11) NOT NULL,
  `titulo` text DEFAULT NULL,
  `descripcion` text NOT NULL,
  `imagen_uri` varchar(500) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `misterios`
--

INSERT INTO `misterios` (`id`, `titulo`, `descripcion`, `imagen_uri`, `created_at`, `updated_at`) VALUES
(10, 'El reloj detenido', 'El señor Ramírez fue encontrado sin vida en su estudio. La habitación estaba cerrada por dentro y no había señales de forcejeo. Sobre el escritorio había un reloj antiguo detenido exactamente a las 10:15 PM. Todos creen que esa fue la hora de la muerte, pero algo no encaja.', 'content://media/picker_get_content/0/com.android.providers.media.photopicker/media/1000176835', '2026-06-24 07:09:23', '2026-06-24 07:09:23');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `password_resets`
--

CREATE TABLE `password_resets` (
  `email` varchar(255) NOT NULL,
  `token` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `personal_access_tokens`
--

CREATE TABLE `personal_access_tokens` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `tokenable_type` varchar(255) NOT NULL,
  `tokenable_id` bigint(20) UNSIGNED NOT NULL,
  `name` varchar(255) NOT NULL,
  `token` varchar(64) NOT NULL,
  `abilities` text DEFAULT NULL,
  `last_used_at` timestamp NULL DEFAULT NULL,
  `expires_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `preguntas`
--

CREATE TABLE `preguntas` (
  `id` int(11) NOT NULL,
  `misterio_id` int(11) NOT NULL,
  `pregunta` text NOT NULL,
  `opcion1` varchar(255) NOT NULL,
  `opcion2` varchar(255) NOT NULL,
  `opcion3` varchar(255) NOT NULL,
  `opcion4` varchar(255) NOT NULL,
  `respuesta_correcta` varchar(255) NOT NULL,
  `pista1` text NOT NULL,
  `pista2` text NOT NULL,
  `pista3` text NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `preguntas`
--

INSERT INTO `preguntas` (`id`, `misterio_id`, `pregunta`, `opcion1`, `opcion2`, `opcion3`, `opcion4`, `respuesta_correcta`, `pista1`, `pista2`, `pista3`, `created_at`, `updated_at`) VALUES
(13, 10, '¿Qué detalle resulta más sospechoso?', 'La habitación cerrada.', 'El reloj detenido', 'El escritorio ordenado', 'La ausencia de testigos', 'El reloj detenido', 'no todo lo que parece una evidencia lo es', 'Alguien pudo manipular un objeto después del crimen', 'El reloj pudo haber sido alterado', '2026-06-24 07:09:28', '2026-06-24 07:09:28'),
(14, 10, '¿Que podría indicar que la hora del reloj es falsa ?', 'El reloj era digital', 'Había pilas nuevas cerca', 'El reloj tenía polvo', 'La ventana estaba abierta', 'Había pilas nuevas cerca', 'Piensa en como funciona un reloj', 'Un objeto cercano contradice la escena', 'Las pilas sugieren manipulación reciente', '2026-06-24 07:09:40', '2026-06-24 07:09:40'),
(15, 10, '¿Cual es la conclusión más lógica ?', 'El reloj marco la hora real', 'Fue un accidente', 'No ocurrió ningún crimen', 'El asesino dejo una pista falsa', 'El asesino dejo una pista falsa', 'los criminales intentar desviar la investigación', 'La hora beneficia al culpable', 'El reloj fue usado para engañar', '2026-06-24 07:09:45', '2026-06-24 07:09:45');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `progreso_misterios`
--

CREATE TABLE `progreso_misterios` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `usuario_id` int(11) NOT NULL,
  `misterio_id` int(11) NOT NULL,
  `aciertos` int(11) DEFAULT 0,
  `porcentaje_real` int(11) DEFAULT 0,
  `porcentaje_aplicado` int(11) DEFAULT 100,
  `completado` tinyint(1) DEFAULT 0,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `progreso_misterios`
--

INSERT INTO `progreso_misterios` (`id`, `usuario_id`, `misterio_id`, `aciertos`, `porcentaje_real`, `porcentaje_aplicado`, `completado`, `created_at`, `updated_at`) VALUES
(8, 1, 7, 1, 100, 100, 1, '2026-06-23 22:53:32', '2026-06-23 22:53:32'),
(9, 1, 8, 1, 100, 95, 1, '2026-06-24 01:49:06', '2026-06-24 01:50:02'),
(10, 2, 9, 1, 100, 95, 1, '2026-06-24 06:32:49', '2026-06-24 06:33:04'),
(11, 2, 10, 3, 100, 100, 1, '2026-06-24 07:16:40', '2026-06-24 07:16:40');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `progreso_preguntas`
--

CREATE TABLE `progreso_preguntas` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `usuario_id` int(11) NOT NULL,
  `misterio_id` int(11) NOT NULL,
  `pregunta_id` int(11) NOT NULL,
  `pistas_utilizadas` int(11) DEFAULT 0,
  `estado` enum('pendiente','correcta','incorrecta','pending','correct','incorrect') NOT NULL DEFAULT 'pendiente',
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `progreso_preguntas`
--

INSERT INTO `progreso_preguntas` (`id`, `usuario_id`, `misterio_id`, `pregunta_id`, `pistas_utilizadas`, `estado`, `created_at`, `updated_at`) VALUES
(12, 1, 7, 10, 0, 'correcta', '2026-06-23 22:53:19', '2026-06-23 22:53:32'),
(13, 1, 8, 11, 1, 'correcta', '2026-06-24 00:43:27', '2026-06-24 01:50:02'),
(14, 2, 9, 12, 1, 'correcta', '2026-06-24 06:30:24', '2026-06-24 06:33:04'),
(15, 2, 10, 13, 0, 'correcta', '2026-06-24 07:10:22', '2026-06-24 07:16:40'),
(16, 2, 10, 14, 0, 'correcta', '2026-06-24 07:10:22', '2026-06-24 07:16:40'),
(17, 2, 10, 15, 0, 'correcta', '2026-06-24 07:10:22', '2026-06-24 07:16:40');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `publicaciones`
--

CREATE TABLE `publicaciones` (
  `id` int(11) NOT NULL,
  `usuario_id` int(11) NOT NULL,
  `misterio_id` int(11) NOT NULL,
  `likes` int(11) DEFAULT 0,
  `fecha_publicacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `publicaciones`
--

INSERT INTO `publicaciones` (`id`, `usuario_id`, `misterio_id`, `likes`, `fecha_publicacion`, `created_at`, `updated_at`) VALUES
(10, 2, 10, 0, '2026-06-24 07:09:28', '2026-06-24 07:09:28', '2026-06-24 07:09:28');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `traducciones`
--

CREATE TABLE `traducciones` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `translatable_type` varchar(255) NOT NULL,
  `translatable_id` bigint(20) UNSIGNED NOT NULL,
  `columna` varchar(255) NOT NULL,
  `idioma` varchar(5) NOT NULL,
  `texto` text NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `traducciones`
--

INSERT INTO `traducciones` (`id`, `translatable_type`, `translatable_id`, `columna`, `idioma`, `texto`, `created_at`, `updated_at`) VALUES
(1, 'App\\Models\\Comentario', 12, 'comentario', 'en', 'hello', '2026-06-23 23:19:42', '2026-06-23 23:19:42'),
(2, 'App\\Models\\Comentario', 12, 'comentario', 'fr', 'bonjour', '2026-06-23 23:19:42', '2026-06-23 23:19:42'),
(3, 'App\\Models\\Comentario', 12, 'comentario', 'pt', 'hola', '2026-06-23 23:19:42', '2026-06-23 23:19:42'),
(4, 'App\\Models\\Comentario', 12, 'comentario', 'en', 'hello', '2026-06-23 23:19:42', '2026-06-23 23:19:42'),
(5, 'App\\Models\\Comentario', 12, 'comentario', 'fr', 'bonjour', '2026-06-23 23:19:42', '2026-06-23 23:19:42'),
(6, 'App\\Models\\Comentario', 12, 'comentario', 'pt', 'hola', '2026-06-23 23:19:42', '2026-06-23 23:19:42'),
(7, 'App\\Models\\Comentario', 13, 'comentario', 'en', 'hello', '2026-06-23 23:20:16', '2026-06-23 23:20:16'),
(8, 'App\\Models\\Comentario', 13, 'comentario', 'fr', 'bonjour', '2026-06-23 23:20:17', '2026-06-23 23:20:17'),
(9, 'App\\Models\\Comentario', 13, 'comentario', 'pt', 'hola', '2026-06-23 23:20:17', '2026-06-23 23:20:17'),
(10, 'App\\Models\\Comentario', 13, 'comentario', 'en', 'hello', '2026-06-23 23:20:17', '2026-06-23 23:20:17'),
(11, 'App\\Models\\Comentario', 13, 'comentario', 'fr', 'bonjour', '2026-06-23 23:20:17', '2026-06-23 23:20:17'),
(12, 'App\\Models\\Comentario', 13, 'comentario', 'pt', 'hola', '2026-06-23 23:20:17', '2026-06-23 23:20:17'),
(13, 'App\\Models\\Comentario', 14, 'comentario', 'en', 'look', '2026-06-23 23:21:25', '2026-06-23 23:21:25'),
(14, 'App\\Models\\Comentario', 14, 'comentario', 'fr', 'regarde', '2026-06-23 23:21:26', '2026-06-23 23:21:26'),
(15, 'App\\Models\\Comentario', 14, 'comentario', 'pt', 'mira', '2026-06-23 23:21:26', '2026-06-23 23:21:26'),
(16, 'App\\Models\\Comentario', 14, 'comentario', 'en', 'look', '2026-06-23 23:21:26', '2026-06-23 23:21:26'),
(17, 'App\\Models\\Comentario', 14, 'comentario', 'fr', 'regarde', '2026-06-23 23:21:28', '2026-06-23 23:21:28'),
(18, 'App\\Models\\Comentario', 14, 'comentario', 'pt', 'mira', '2026-06-23 23:21:28', '2026-06-23 23:21:28'),
(19, 'App\\Models\\Misterio', 8, 'titulo', 'en', 'mystery', '2026-06-24 00:42:05', '2026-06-24 00:42:05'),
(20, 'App\\Models\\Misterio', 8, 'descripcion', 'en', 'a mystery', '2026-06-24 00:42:05', '2026-06-24 00:42:05'),
(21, 'App\\Models\\Misterio', 8, 'titulo', 'fr', 'mystère', '2026-06-24 00:42:05', '2026-06-24 00:42:05'),
(22, 'App\\Models\\Misterio', 8, 'descripcion', 'fr', 'un mystère', '2026-06-24 00:42:07', '2026-06-24 00:42:07'),
(23, 'App\\Models\\Misterio', 8, 'titulo', 'pt', 'mistério', '2026-06-24 00:42:07', '2026-06-24 00:42:07'),
(24, 'App\\Models\\Misterio', 8, 'descripcion', 'pt', 'um mistério', '2026-06-24 00:42:08', '2026-06-24 00:42:08'),
(25, 'App\\Models\\Pregunta', 11, 'pregunta', 'en', 'question 1', '2026-06-24 00:42:09', '2026-06-24 00:42:09'),
(26, 'App\\Models\\Pregunta', 11, 'opcion1', 'en', '1', '2026-06-24 00:42:09', '2026-06-24 00:42:09'),
(27, 'App\\Models\\Pregunta', 11, 'opcion2', 'en', '2', '2026-06-24 00:42:09', '2026-06-24 00:42:09'),
(28, 'App\\Models\\Pregunta', 11, 'opcion3', 'en', '3', '2026-06-24 00:42:09', '2026-06-24 00:42:09'),
(29, 'App\\Models\\Pregunta', 11, 'opcion4', 'en', '4', '2026-06-24 00:42:10', '2026-06-24 00:42:10'),
(30, 'App\\Models\\Pregunta', 11, 'pista1', 'en', '3', '2026-06-24 00:42:11', '2026-06-24 00:42:11'),
(31, 'App\\Models\\Pregunta', 11, 'pista2', 'en', '2', '2026-06-24 00:42:12', '2026-06-24 00:42:12'),
(32, 'App\\Models\\Pregunta', 11, 'pista3', 'en', '1', '2026-06-24 00:42:12', '2026-06-24 00:42:12'),
(33, 'App\\Models\\Pregunta', 11, 'pregunta', 'fr', 'première question', '2026-06-24 00:42:12', '2026-06-24 00:42:12'),
(34, 'App\\Models\\Pregunta', 11, 'opcion1', 'fr', '1', '2026-06-24 00:42:13', '2026-06-24 00:42:13'),
(35, 'App\\Models\\Pregunta', 11, 'opcion2', 'fr', '2', '2026-06-24 00:42:14', '2026-06-24 00:42:14'),
(36, 'App\\Models\\Pregunta', 11, 'opcion3', 'fr', '3', '2026-06-24 00:42:14', '2026-06-24 00:42:14'),
(37, 'App\\Models\\Pregunta', 11, 'opcion4', 'fr', '4', '2026-06-24 00:42:14', '2026-06-24 00:42:14'),
(38, 'App\\Models\\Pregunta', 11, 'pista1', 'fr', '3', '2026-06-24 00:42:14', '2026-06-24 00:42:14'),
(39, 'App\\Models\\Pregunta', 11, 'pista2', 'fr', '2', '2026-06-24 00:42:14', '2026-06-24 00:42:14'),
(40, 'App\\Models\\Pregunta', 11, 'pista3', 'fr', '1', '2026-06-24 00:42:14', '2026-06-24 00:42:14'),
(41, 'App\\Models\\Pregunta', 11, 'pregunta', 'pt', 'pergunta 1', '2026-06-24 00:42:15', '2026-06-24 00:42:15'),
(42, 'App\\Models\\Pregunta', 11, 'opcion1', 'pt', '1', '2026-06-24 00:42:15', '2026-06-24 00:42:15'),
(43, 'App\\Models\\Pregunta', 11, 'opcion2', 'pt', '2', '2026-06-24 00:42:15', '2026-06-24 00:42:15'),
(44, 'App\\Models\\Pregunta', 11, 'opcion3', 'pt', '3', '2026-06-24 00:42:15', '2026-06-24 00:42:15'),
(45, 'App\\Models\\Pregunta', 11, 'opcion4', 'pt', '4', '2026-06-24 00:42:15', '2026-06-24 00:42:15'),
(46, 'App\\Models\\Pregunta', 11, 'pista1', 'pt', '3', '2026-06-24 00:42:15', '2026-06-24 00:42:15'),
(47, 'App\\Models\\Pregunta', 11, 'pista2', 'pt', '2', '2026-06-24 00:42:15', '2026-06-24 00:42:15'),
(48, 'App\\Models\\Pregunta', 11, 'pista3', 'pt', '1', '2026-06-24 00:42:15', '2026-06-24 00:42:15'),
(49, 'App\\Models\\Misterio', 9, 'titulo', 'en', 'test mystery', '2026-06-24 05:08:12', '2026-06-24 05:08:12'),
(50, 'App\\Models\\Misterio', 9, 'descripcion', 'en', 'mystery to test functionality', '2026-06-24 05:08:12', '2026-06-24 05:08:12'),
(51, 'App\\Models\\Misterio', 9, 'titulo', 'fr', 'mystère de test', '2026-06-24 05:08:14', '2026-06-24 05:08:14'),
(52, 'App\\Models\\Misterio', 9, 'descripcion', 'fr', 'mystère pour tester la fonctionnalité', '2026-06-24 05:08:15', '2026-06-24 05:08:15'),
(53, 'App\\Models\\Misterio', 9, 'titulo', 'pt', 'mistério de teste', '2026-06-24 05:08:16', '2026-06-24 05:08:16'),
(54, 'App\\Models\\Misterio', 9, 'descripcion', 'pt', 'mistério para testar funcionalidade', '2026-06-24 05:08:17', '2026-06-24 05:08:17'),
(55, 'App\\Models\\Pregunta', 12, 'pregunta', 'en', 'question 1', '2026-06-24 05:08:18', '2026-06-24 05:08:18'),
(56, 'App\\Models\\Pregunta', 12, 'opcion1', 'en', '1', '2026-06-24 05:08:20', '2026-06-24 05:08:20'),
(57, 'App\\Models\\Pregunta', 12, 'opcion2', 'en', '2', '2026-06-24 05:08:20', '2026-06-24 05:08:20'),
(58, 'App\\Models\\Pregunta', 12, 'opcion3', 'en', '3', '2026-06-24 05:08:20', '2026-06-24 05:08:20'),
(59, 'App\\Models\\Pregunta', 12, 'opcion4', 'en', '4', '2026-06-24 05:08:20', '2026-06-24 05:08:20'),
(60, 'App\\Models\\Pregunta', 12, 'pista1', 'en', 'e', '2026-06-24 05:08:20', '2026-06-24 05:08:20'),
(61, 'App\\Models\\Pregunta', 12, 'pista2', 'en', 't', '2026-06-24 05:08:20', '2026-06-24 05:08:20'),
(62, 'App\\Models\\Pregunta', 12, 'pista3', 'en', '1', '2026-06-24 05:08:20', '2026-06-24 05:08:20'),
(63, 'App\\Models\\Pregunta', 12, 'pregunta', 'fr', 'première question', '2026-06-24 05:08:21', '2026-06-24 05:08:21'),
(64, 'App\\Models\\Pregunta', 12, 'opcion1', 'fr', '1', '2026-06-24 05:08:21', '2026-06-24 05:08:21'),
(65, 'App\\Models\\Pregunta', 12, 'opcion2', 'fr', '2', '2026-06-24 05:08:21', '2026-06-24 05:08:21'),
(66, 'App\\Models\\Pregunta', 12, 'opcion3', 'fr', '3', '2026-06-24 05:08:21', '2026-06-24 05:08:21'),
(67, 'App\\Models\\Pregunta', 12, 'opcion4', 'fr', '4', '2026-06-24 05:08:22', '2026-06-24 05:08:22'),
(68, 'App\\Models\\Pregunta', 12, 'pista1', 'fr', 'e', '2026-06-24 05:08:22', '2026-06-24 05:08:22'),
(69, 'App\\Models\\Pregunta', 12, 'pista2', 'fr', 't', '2026-06-24 05:08:22', '2026-06-24 05:08:22'),
(70, 'App\\Models\\Pregunta', 12, 'pista3', 'fr', '1', '2026-06-24 05:08:22', '2026-06-24 05:08:22'),
(71, 'App\\Models\\Pregunta', 12, 'pregunta', 'pt', 'pergunta 1', '2026-06-24 05:08:22', '2026-06-24 05:08:22'),
(72, 'App\\Models\\Pregunta', 12, 'opcion1', 'pt', '1', '2026-06-24 05:08:23', '2026-06-24 05:08:23'),
(73, 'App\\Models\\Pregunta', 12, 'opcion2', 'pt', '2', '2026-06-24 05:08:23', '2026-06-24 05:08:23'),
(74, 'App\\Models\\Pregunta', 12, 'opcion3', 'pt', '3', '2026-06-24 05:08:23', '2026-06-24 05:08:23'),
(75, 'App\\Models\\Pregunta', 12, 'opcion4', 'pt', '4', '2026-06-24 05:08:23', '2026-06-24 05:08:23'),
(76, 'App\\Models\\Pregunta', 12, 'pista1', 'pt', 'e', '2026-06-24 05:08:23', '2026-06-24 05:08:23'),
(77, 'App\\Models\\Pregunta', 12, 'pista2', 'pt', 't', '2026-06-24 05:08:23', '2026-06-24 05:08:23'),
(78, 'App\\Models\\Pregunta', 12, 'pista3', 'pt', '1', '2026-06-24 05:08:23', '2026-06-24 05:08:23'),
(79, 'App\\Models\\Comentario', 15, 'comentario', 'en', 'hello world', '2026-06-24 06:36:36', '2026-06-24 06:36:36'),
(80, 'App\\Models\\Comentario', 15, 'comentario', 'fr', 'bonjour monde', '2026-06-24 06:36:38', '2026-06-24 06:36:38'),
(81, 'App\\Models\\Comentario', 15, 'comentario', 'pt', 'hola mundo', '2026-06-24 06:36:38', '2026-06-24 06:36:38'),
(82, 'App\\Models\\Comentario', 15, 'comentario', 'en', 'hello world', '2026-06-24 06:36:38', '2026-06-24 06:36:38'),
(83, 'App\\Models\\Comentario', 15, 'comentario', 'fr', 'bonjour monde', '2026-06-24 06:36:39', '2026-06-24 06:36:39'),
(84, 'App\\Models\\Comentario', 15, 'comentario', 'pt', 'hola mundo', '2026-06-24 06:36:39', '2026-06-24 06:36:39'),
(85, 'App\\Models\\Misterio', 10, 'titulo', 'en', 'The watch stopped', '2026-06-24 07:09:25', '2026-06-24 07:09:25'),
(86, 'App\\Models\\Misterio', 10, 'descripcion', 'en', 'Mr. Ramirez was found lifeless in his study. The room was closed inside and there was no sign of a struggle. On the desk was an old watch stopped at exactly 10: 15 PM. Everyone thinks that was the time of death, but something doesn\'t fit.', '2026-06-24 07:09:26', '2026-06-24 07:09:26'),
(87, 'App\\Models\\Misterio', 10, 'titulo', 'fr', 'La montre s\'est arrêtée', '2026-06-24 07:09:26', '2026-06-24 07:09:26'),
(88, 'App\\Models\\Misterio', 10, 'descripcion', 'fr', 'M. Ramirez a été trouvé sans vie dans son étude. La chambre était fermée à l\'intérieur et il n\'y avait aucun signe de lutte. Sur le bureau, une vieille montre s\'arrêtait exactement à 22h15. Tout le monde pense que c\'était le moment de la mort, mais quelque chose ne va pas.', '2026-06-24 07:09:28', '2026-06-24 07:09:28'),
(89, 'App\\Models\\Misterio', 10, 'titulo', 'pt', 'O relógio parado', '2026-06-24 07:09:28', '2026-06-24 07:09:28'),
(90, 'App\\Models\\Misterio', 10, 'descripcion', 'pt', 'O senhor Ramírez foi encontrado sem vida em seu estudo. O quarto estava fechado por dentro e não havia sinais de forcejeo. Sobre o ecrã havia um relógio antigo detido exatamente às 10:15 PM. Todos acreditam que essa foi a hora da morte, mas algo não se encaixa.', '2026-06-24 07:09:28', '2026-06-24 07:09:28'),
(91, 'App\\Models\\Pregunta', 13, 'pregunta', 'en', 'What detail is more suspicious?', '2026-06-24 07:09:30', '2026-06-24 07:09:30'),
(92, 'App\\Models\\Pregunta', 13, 'opcion1', 'en', 'Room closed.', '2026-06-24 07:09:30', '2026-06-24 07:09:30'),
(93, 'App\\Models\\Pregunta', 13, 'opcion2', 'en', 'The watch stopped', '2026-06-24 07:09:30', '2026-06-24 07:09:30'),
(94, 'App\\Models\\Pregunta', 13, 'opcion3', 'en', 'The ordered desk', '2026-06-24 07:09:32', '2026-06-24 07:09:32'),
(95, 'App\\Models\\Pregunta', 13, 'opcion4', 'en', 'The absence of witnesses', '2026-06-24 07:09:32', '2026-06-24 07:09:32'),
(96, 'App\\Models\\Pregunta', 13, 'pista1', 'en', 'not everything that looks like evidence is', '2026-06-24 07:09:32', '2026-06-24 07:09:32'),
(97, 'App\\Models\\Pregunta', 13, 'pista2', 'en', 'Someone could have manipulated an object after the crime', '2026-06-24 07:09:33', '2026-06-24 07:09:33'),
(98, 'App\\Models\\Pregunta', 13, 'pista3', 'en', 'The clock may have been altered', '2026-06-24 07:09:34', '2026-06-24 07:09:34'),
(99, 'App\\Models\\Pregunta', 13, 'pregunta', 'fr', 'Quel détail est plus suspect?', '2026-06-24 07:09:35', '2026-06-24 07:09:35'),
(100, 'App\\Models\\Pregunta', 13, 'opcion1', 'fr', 'Chambre fermée.', '2026-06-24 07:09:35', '2026-06-24 07:09:35'),
(101, 'App\\Models\\Pregunta', 13, 'opcion2', 'fr', 'La montre s\'est arrêtée', '2026-06-24 07:09:36', '2026-06-24 07:09:36'),
(102, 'App\\Models\\Pregunta', 13, 'opcion3', 'fr', 'Le bureau commandé', '2026-06-24 07:09:36', '2026-06-24 07:09:36'),
(103, 'App\\Models\\Pregunta', 13, 'opcion4', 'fr', 'L \' absence de témoins', '2026-06-24 07:09:36', '2026-06-24 07:09:36'),
(104, 'App\\Models\\Pregunta', 13, 'pista1', 'fr', 'tout ce qui ressemble à des preuves n\'est pas', '2026-06-24 07:09:37', '2026-06-24 07:09:37'),
(105, 'App\\Models\\Pregunta', 13, 'pista2', 'fr', 'Quelqu\'un aurait pu manipuler un objet après le crime', '2026-06-24 07:09:37', '2026-06-24 07:09:37'),
(106, 'App\\Models\\Pregunta', 13, 'pista3', 'fr', 'L\'horloge a peut-être été modifiée', '2026-06-24 07:09:38', '2026-06-24 07:09:38'),
(107, 'App\\Models\\Pregunta', 13, 'pregunta', 'pt', 'Que detalhe é mais suspeito?', '2026-06-24 07:09:38', '2026-06-24 07:09:38'),
(108, 'App\\Models\\Pregunta', 13, 'opcion1', 'pt', 'O quarto fechado.', '2026-06-24 07:09:38', '2026-06-24 07:09:38'),
(109, 'App\\Models\\Pregunta', 13, 'opcion2', 'pt', 'O relógio parado', '2026-06-24 07:09:38', '2026-06-24 07:09:38'),
(110, 'App\\Models\\Pregunta', 13, 'opcion3', 'pt', 'O Ecrã Ordenado', '2026-06-24 07:09:39', '2026-06-24 07:09:39'),
(111, 'App\\Models\\Pregunta', 13, 'opcion4', 'pt', 'A ausência de testemunhas', '2026-06-24 07:09:39', '2026-06-24 07:09:39'),
(112, 'App\\Models\\Pregunta', 13, 'pista1', 'pt', 'nem tudo o que parece uma evidência é', '2026-06-24 07:09:39', '2026-06-24 07:09:39'),
(113, 'App\\Models\\Pregunta', 13, 'pista2', 'pt', 'Alguém conseguiu manipular um objeto após o crime', '2026-06-24 07:09:40', '2026-06-24 07:09:40'),
(114, 'App\\Models\\Pregunta', 13, 'pista3', 'pt', 'O relógio pode ter sido alterado', '2026-06-24 07:09:40', '2026-06-24 07:09:40'),
(115, 'App\\Models\\Pregunta', 14, 'pregunta', 'en', 'What could you say the clock time is false?', '2026-06-24 07:09:40', '2026-06-24 07:09:40'),
(116, 'App\\Models\\Pregunta', 14, 'opcion1', 'en', 'The watch was digital', '2026-06-24 07:09:40', '2026-06-24 07:09:40'),
(117, 'App\\Models\\Pregunta', 14, 'opcion2', 'en', 'There were new batteries nearby', '2026-06-24 07:09:40', '2026-06-24 07:09:40'),
(118, 'App\\Models\\Pregunta', 14, 'opcion3', 'en', 'The clock had dust', '2026-06-24 07:09:41', '2026-06-24 07:09:41'),
(119, 'App\\Models\\Pregunta', 14, 'opcion4', 'en', 'The window was open', '2026-06-24 07:09:41', '2026-06-24 07:09:41'),
(120, 'App\\Models\\Pregunta', 14, 'pista1', 'en', 'Think about how a watch works', '2026-06-24 07:09:41', '2026-06-24 07:09:41'),
(121, 'App\\Models\\Pregunta', 14, 'pista2', 'en', 'A nearby object contradicts the scene', '2026-06-24 07:09:41', '2026-06-24 07:09:41'),
(122, 'App\\Models\\Pregunta', 14, 'pista3', 'en', 'The batteries suggest recent manipulation', '2026-06-24 07:09:42', '2026-06-24 07:09:42'),
(123, 'App\\Models\\Pregunta', 14, 'pregunta', 'fr', 'Que diriez-vous que l\'heure soit fausse ?', '2026-06-24 07:09:42', '2026-06-24 07:09:42'),
(124, 'App\\Models\\Pregunta', 14, 'opcion1', 'fr', 'La montre était numérique', '2026-06-24 07:09:42', '2026-06-24 07:09:42'),
(125, 'App\\Models\\Pregunta', 14, 'opcion2', 'fr', 'Il y avait de nouvelles batteries à proximité', '2026-06-24 07:09:42', '2026-06-24 07:09:42'),
(126, 'App\\Models\\Pregunta', 14, 'opcion3', 'fr', 'L\'horloge avait de la poussière', '2026-06-24 07:09:43', '2026-06-24 07:09:43'),
(127, 'App\\Models\\Pregunta', 14, 'opcion4', 'fr', 'La fenêtre était ouverte', '2026-06-24 07:09:43', '2026-06-24 07:09:43'),
(128, 'App\\Models\\Pregunta', 14, 'pista1', 'fr', 'Pensez à comment fonctionne une montre', '2026-06-24 07:09:43', '2026-06-24 07:09:43'),
(129, 'App\\Models\\Pregunta', 14, 'pista2', 'fr', 'Un objet voisin contredit la scène', '2026-06-24 07:09:44', '2026-06-24 07:09:44'),
(130, 'App\\Models\\Pregunta', 14, 'pista3', 'fr', 'Les batteries suggèrent une manipulation récente', '2026-06-24 07:09:44', '2026-06-24 07:09:44'),
(131, 'App\\Models\\Pregunta', 14, 'pregunta', 'pt', 'O que poderia indicar que a hora do relógio é falsa?', '2026-06-24 07:09:44', '2026-06-24 07:09:44'),
(132, 'App\\Models\\Pregunta', 14, 'opcion1', 'pt', 'O relógio era digital', '2026-06-24 07:09:44', '2026-06-24 07:09:44'),
(133, 'App\\Models\\Pregunta', 14, 'opcion2', 'pt', 'Havia pilhas novas perto', '2026-06-24 07:09:45', '2026-06-24 07:09:45'),
(134, 'App\\Models\\Pregunta', 14, 'opcion3', 'pt', 'O relógio tinha pó', '2026-06-24 07:09:45', '2026-06-24 07:09:45'),
(135, 'App\\Models\\Pregunta', 14, 'opcion4', 'pt', 'A janela estava aberta', '2026-06-24 07:09:45', '2026-06-24 07:09:45'),
(136, 'App\\Models\\Pregunta', 14, 'pista1', 'pt', 'Pense em como funciona um relógio', '2026-06-24 07:09:45', '2026-06-24 07:09:45'),
(137, 'App\\Models\\Pregunta', 14, 'pista2', 'pt', 'Um objeto próximo contradiz a cena', '2026-06-24 07:09:45', '2026-06-24 07:09:45'),
(138, 'App\\Models\\Pregunta', 14, 'pista3', 'pt', 'As pilhas sugerem manipulação recente', '2026-06-24 07:09:45', '2026-06-24 07:09:45'),
(139, 'App\\Models\\Pregunta', 15, 'pregunta', 'en', 'What is the most logical conclusion?', '2026-06-24 07:09:45', '2026-06-24 07:09:45'),
(140, 'App\\Models\\Pregunta', 15, 'opcion1', 'en', 'The clock frame the real time', '2026-06-24 07:09:45', '2026-06-24 07:09:45'),
(141, 'App\\Models\\Pregunta', 15, 'opcion2', 'en', 'It was an accident', '2026-06-24 07:09:46', '2026-06-24 07:09:46'),
(142, 'App\\Models\\Pregunta', 15, 'opcion3', 'en', 'No crime happened', '2026-06-24 07:09:46', '2026-06-24 07:09:46'),
(143, 'App\\Models\\Pregunta', 15, 'opcion4', 'en', 'The killer left a false lead', '2026-06-24 07:09:46', '2026-06-24 07:09:46'),
(144, 'App\\Models\\Pregunta', 15, 'pista1', 'en', 'the criminals try to divert the investigation', '2026-06-24 07:09:46', '2026-06-24 07:09:46'),
(145, 'App\\Models\\Pregunta', 15, 'pista2', 'en', 'The time benefits the guilty', '2026-06-24 07:09:46', '2026-06-24 07:09:46'),
(146, 'App\\Models\\Pregunta', 15, 'pista3', 'en', 'The watch was used to cheat', '2026-06-24 07:09:46', '2026-06-24 07:09:46'),
(147, 'App\\Models\\Pregunta', 15, 'pregunta', 'fr', 'Quelle est la conclusion la plus logique?', '2026-06-24 07:09:47', '2026-06-24 07:09:47'),
(148, 'App\\Models\\Pregunta', 15, 'opcion1', 'fr', 'L\'horloge cadre le temps réel', '2026-06-24 07:09:47', '2026-06-24 07:09:47'),
(149, 'App\\Models\\Pregunta', 15, 'opcion2', 'fr', 'C\'était un accident', '2026-06-24 07:09:47', '2026-06-24 07:09:47'),
(150, 'App\\Models\\Pregunta', 15, 'opcion3', 'fr', 'Aucun crime n\'est arrivé', '2026-06-24 07:09:47', '2026-06-24 07:09:47'),
(151, 'App\\Models\\Pregunta', 15, 'opcion4', 'fr', 'Le tueur a laissé une fausse piste', '2026-06-24 07:09:48', '2026-06-24 07:09:48'),
(152, 'App\\Models\\Pregunta', 15, 'pista1', 'fr', 'les criminels essaient de détourner l\'enquête', '2026-06-24 07:09:48', '2026-06-24 07:09:48'),
(153, 'App\\Models\\Pregunta', 15, 'pista2', 'fr', 'Le temps profite aux coupables', '2026-06-24 07:09:48', '2026-06-24 07:09:48'),
(154, 'App\\Models\\Pregunta', 15, 'pista3', 'fr', 'La montre était utilisée pour tricher', '2026-06-24 07:09:49', '2026-06-24 07:09:49'),
(155, 'App\\Models\\Pregunta', 15, 'pregunta', 'pt', 'Qual é a conclusão mais lógica?', '2026-06-24 07:09:49', '2026-06-24 07:09:49'),
(156, 'App\\Models\\Pregunta', 15, 'opcion1', 'pt', 'O relógio-quadro a hora real', '2026-06-24 07:09:49', '2026-06-24 07:09:49'),
(157, 'App\\Models\\Pregunta', 15, 'opcion2', 'pt', 'Foi um acidente', '2026-06-24 07:09:49', '2026-06-24 07:09:49'),
(158, 'App\\Models\\Pregunta', 15, 'opcion3', 'pt', 'Não ocorreu nenhum crime', '2026-06-24 07:09:49', '2026-06-24 07:09:49'),
(159, 'App\\Models\\Pregunta', 15, 'opcion4', 'pt', 'O assassino deixo uma pista falsa', '2026-06-24 07:09:49', '2026-06-24 07:09:49'),
(160, 'App\\Models\\Pregunta', 15, 'pista1', 'pt', 'os criminosos tentar desviar a investigação', '2026-06-24 07:09:49', '2026-06-24 07:09:49'),
(161, 'App\\Models\\Pregunta', 15, 'pista2', 'pt', 'A hora beneficia o culpado', '2026-06-24 07:09:50', '2026-06-24 07:09:50'),
(162, 'App\\Models\\Pregunta', 15, 'pista3', 'pt', 'O relógio foi usado para enganar', '2026-06-24 07:09:50', '2026-06-24 07:09:50');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users`
--

CREATE TABLE `users` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `email_verified_at` timestamp NULL DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `remember_token` varchar(100) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id` int(11) NOT NULL,
  `firebase_uid` varchar(255) DEFAULT NULL,
  `nombre` varchar(100) NOT NULL,
  `email` varchar(150) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `fecha_registro` timestamp NOT NULL DEFAULT current_timestamp(),
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `foto` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id`, `firebase_uid`, `nombre`, `email`, `password`, `fecha_registro`, `created_at`, `updated_at`, `foto`) VALUES
(1, NULL, 'jasson', 'jasson@gmail.com', '12345', '2026-06-21 00:42:50', NULL, NULL, ''),
(2, 'C6E0z6uko0aQ8CJf3wpWs5I6qEE3', 'jasson 2.0', 'kerrycapija043@gmail.com', '$2y$10$CoTPj0OP4f6k86dpQMiU7ugm18Pw/wd2etejgJayvCRsKHuUdpqjW', '2026-06-23 06:00:00', '2026-06-24 04:28:41', '2026-06-24 04:28:41', 'default_avatar.png');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `comentarios`
--
ALTER TABLE `comentarios`
  ADD PRIMARY KEY (`id`),
  ADD KEY `publicacion_id` (`publicacion_id`),
  ADD KEY `fk_comentarios_usuarios` (`usuario_id`);

--
-- Indices de la tabla `failed_jobs`
--
ALTER TABLE `failed_jobs`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `failed_jobs_uuid_unique` (`uuid`);

--
-- Indices de la tabla `likes`
--
ALTER TABLE `likes`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `likes_usuario_publicacion_unique` (`usuario_id`,`publicacion_id`);

--
-- Indices de la tabla `migrations`
--
ALTER TABLE `migrations`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `misterios`
--
ALTER TABLE `misterios`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `password_resets`
--
ALTER TABLE `password_resets`
  ADD PRIMARY KEY (`email`);

--
-- Indices de la tabla `personal_access_tokens`
--
ALTER TABLE `personal_access_tokens`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `personal_access_tokens_token_unique` (`token`),
  ADD KEY `personal_access_tokens_tokenable_type_tokenable_id_index` (`tokenable_type`,`tokenable_id`);

--
-- Indices de la tabla `preguntas`
--
ALTER TABLE `preguntas`
  ADD PRIMARY KEY (`id`),
  ADD KEY `misterio_id` (`misterio_id`);

--
-- Indices de la tabla `progreso_misterios`
--
ALTER TABLE `progreso_misterios`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `user_misterio_unique` (`usuario_id`,`misterio_id`);

--
-- Indices de la tabla `progreso_preguntas`
--
ALTER TABLE `progreso_preguntas`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `user_pregunta_unique` (`usuario_id`,`pregunta_id`);

--
-- Indices de la tabla `publicaciones`
--
ALTER TABLE `publicaciones`
  ADD PRIMARY KEY (`id`),
  ADD KEY `usuario_id` (`usuario_id`),
  ADD KEY `misterio_id` (`misterio_id`);

--
-- Indices de la tabla `traducciones`
--
ALTER TABLE `traducciones`
  ADD PRIMARY KEY (`id`),
  ADD KEY `traducciones_translatable_type_translatable_id_index` (`translatable_type`,`translatable_id`),
  ADD KEY `idx_traducciones` (`translatable_type`,`translatable_id`,`columna`,`idioma`);

--
-- Indices de la tabla `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `users_email_unique` (`email`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD UNIQUE KEY `usuarios_firebase_uid_unique` (`firebase_uid`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `comentarios`
--
ALTER TABLE `comentarios`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT de la tabla `failed_jobs`
--
ALTER TABLE `failed_jobs`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `likes`
--
ALTER TABLE `likes`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT de la tabla `migrations`
--
ALTER TABLE `migrations`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT de la tabla `misterios`
--
ALTER TABLE `misterios`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `personal_access_tokens`
--
ALTER TABLE `personal_access_tokens`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `preguntas`
--
ALTER TABLE `preguntas`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT de la tabla `progreso_misterios`
--
ALTER TABLE `progreso_misterios`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT de la tabla `progreso_preguntas`
--
ALTER TABLE `progreso_preguntas`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT de la tabla `publicaciones`
--
ALTER TABLE `publicaciones`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `traducciones`
--
ALTER TABLE `traducciones`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=163;

--
-- AUTO_INCREMENT de la tabla `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `comentarios`
--
ALTER TABLE `comentarios`
  ADD CONSTRAINT `comentarios_ibfk_1` FOREIGN KEY (`publicacion_id`) REFERENCES `publicaciones` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_comentarios_usuarios` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `preguntas`
--
ALTER TABLE `preguntas`
  ADD CONSTRAINT `preguntas_ibfk_1` FOREIGN KEY (`misterio_id`) REFERENCES `misterios` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `publicaciones`
--
ALTER TABLE `publicaciones`
  ADD CONSTRAINT `publicaciones_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `publicaciones_ibfk_2` FOREIGN KEY (`misterio_id`) REFERENCES `misterios` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
