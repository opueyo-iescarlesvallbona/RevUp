USE [master]
GO
/****** Object:  Database [revup]    Script Date: 4/1/2025 4:18:58 PM ******/
CREATE DATABASE [revup]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'revup', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.SQLEXPRESS\MSSQL\DATA\revup.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'revup_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.SQLEXPRESS\MSSQL\DATA\revup_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT, LEDGER = OFF
GO
ALTER DATABASE [revup] SET COMPATIBILITY_LEVEL = 160
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [revup].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [revup] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [revup] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [revup] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [revup] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [revup] SET ARITHABORT OFF 
GO
ALTER DATABASE [revup] SET AUTO_CLOSE ON 
GO
ALTER DATABASE [revup] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [revup] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [revup] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [revup] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [revup] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [revup] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [revup] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [revup] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [revup] SET  ENABLE_BROKER 
GO
ALTER DATABASE [revup] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [revup] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [revup] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [revup] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [revup] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [revup] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [revup] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [revup] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [revup] SET  MULTI_USER 
GO
ALTER DATABASE [revup] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [revup] SET DB_CHAINING OFF 
GO
ALTER DATABASE [revup] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [revup] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [revup] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [revup] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
ALTER DATABASE [revup] SET QUERY_STORE = ON
GO
ALTER DATABASE [revup] SET QUERY_STORE (OPERATION_MODE = READ_WRITE, CLEANUP_POLICY = (STALE_QUERY_THRESHOLD_DAYS = 30), DATA_FLUSH_INTERVAL_SECONDS = 900, INTERVAL_LENGTH_MINUTES = 60, MAX_STORAGE_SIZE_MB = 1000, QUERY_CAPTURE_MODE = AUTO, SIZE_BASED_CLEANUP_MODE = AUTO, MAX_PLANS_PER_QUERY = 200, WAIT_STATS_CAPTURE_MODE = ON)
GO
USE [revup]
GO
/****** Object:  User [founder]    Script Date: 4/1/2025 4:18:58 PM ******/
CREATE USER [founder] FOR LOGIN [founder] WITH DEFAULT_SCHEMA=[dbo]
GO
ALTER ROLE [db_owner] ADD MEMBER [founder]
GO
/****** Object:  Table [dbo].[brand]    Script Date: 4/1/2025 4:18:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[brand](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [varchar](100) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[car]    Script Date: 4/1/2025 4:18:59 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[car](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[brand] [varchar](100) NOT NULL,
	[model] [varchar](200) NOT NULL,
	[model_year] [int] NOT NULL,
	[description] [text] NULL,
	[member_id] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[club]    Script Date: 4/1/2025 4:18:59 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[club](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [varchar](100) NOT NULL,
	[founder] [int] NOT NULL,
	[description] [text] NULL,
	[picture] [varbinary](max) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[club_event]    Script Date: 4/1/2025 4:18:59 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[club_event](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [varchar](100) NOT NULL,
	[address] [varchar](255) NOT NULL,
	[club_id] [int] NOT NULL,
	[picture] [varbinary](max) NULL,
	[start_date] [datetime] NOT NULL,
	[route_start_date] [datetime] NULL,
	[end_date] [datetime] NOT NULL,
	[description] [text] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[gender]    Script Date: 4/1/2025 4:18:59 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[gender](
	[id] [int] NOT NULL,
	[name] [varchar](50) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[member]    Script Date: 4/1/2025 4:18:59 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[member](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [varchar](100) NOT NULL,
	[membername] [varchar](50) NULL,
	[experience] [int] NULL,
	[email] [varchar](100) NOT NULL,
	[gender_id] [int] NOT NULL,
	[location_id] [int] NOT NULL,
	[date_of_birth] [date] NOT NULL,
	[login_date] [date] NOT NULL,
	[description] [text] NULL,
	[profile_picture] [varbinary](max) NULL,
	[password] [text] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[member_club]    Script Date: 4/1/2025 4:18:59 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[member_club](
	[member_id] [int] NOT NULL,
	[club_id] [int] NOT NULL,
	[role_type] [int] NOT NULL,
	[join_date] [date] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[member_id] ASC,
	[club_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[member_club_role]    Script Date: 4/1/2025 4:18:59 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[member_club_role](
	[id] [int] NOT NULL,
	[name] [varchar](100) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[member_location]    Script Date: 4/1/2025 4:18:59 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[member_location](
	[id] [int] NOT NULL,
	[municipality] [varchar](200) NOT NULL,
	[ccaa] [varchar](100) NULL,
	[country] [varchar](100) NOT NULL,
	[latitude] [float] NOT NULL,
	[longitude] [float] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[member_relation]    Script Date: 4/1/2025 4:18:59 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[member_relation](
	[member_id1] [int] NOT NULL,
	[member_id2] [int] NOT NULL,
	[state_id] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[member_id1] ASC,
	[member_id2] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[message]    Script Date: 4/1/2025 4:18:59 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[message](
	[sender_id] [int] NOT NULL,
	[receiver_id] [int] NOT NULL,
	[datetime] [datetime] NOT NULL,
	[content_message] [text] NOT NULL,
	[state_id] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[sender_id] ASC,
	[receiver_id] ASC,
	[datetime] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[message_state]    Script Date: 4/1/2025 4:18:59 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[message_state](
	[id] [int] NOT NULL,
	[name] [varchar](50) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[model]    Script Date: 4/1/2025 4:18:59 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[model](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[id_brand] [int] NOT NULL,
	[model_name] [varchar](200) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[post]    Script Date: 4/1/2025 4:18:59 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[post](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[title] [varchar](200) NOT NULL,
	[post_type] [int] NOT NULL,
	[description] [text] NULL,
	[post_date] [datetime] NOT NULL,
	[picture] [varbinary](max) NULL,
	[likes] [bigint] NOT NULL,
	[address] [varchar](100) NULL,
	[route_id] [int] NULL,
	[member_id] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[post_comment]    Script Date: 4/1/2025 4:18:59 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[post_comment](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[post_id] [int] NOT NULL,
	[member_id] [int] NOT NULL,
	[comment_content] [text] NOT NULL,
	[datetime] [datetime] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[post_type]    Script Date: 4/1/2025 4:18:59 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[post_type](
	[id] [int] NOT NULL,
	[name] [varchar](100) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[relation_state]    Script Date: 4/1/2025 4:18:59 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[relation_state](
	[id] [int] NOT NULL,
	[name] [varchar](50) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[route]    Script Date: 4/1/2025 4:18:59 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[route](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [varchar](100) NOT NULL,
	[waypoints] [text] NOT NULL,
	[distance] [numeric](12, 2) NOT NULL,
	[duration] [bigint] NOT NULL,
	[max_elevation] [numeric](6, 2) NULL,
	[elevation_gain] [numeric](6, 2) NULL,
	[start_address] [varchar](200) NOT NULL,
	[end_address] [varchar](200) NOT NULL,
	[terrain_type_id] [int] NULL,
	[description] [text] NULL,
	[member_id] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[terrain_type]    Script Date: 4/1/2025 4:18:59 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[terrain_type](
	[id] [int] NOT NULL,
	[name] [varchar](50) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[member_club] ADD  DEFAULT (getdate()) FOR [join_date]
GO
ALTER TABLE [dbo].[route] ADD  DEFAULT (getdate()) FOR [name]
GO
ALTER TABLE [dbo].[car]  WITH CHECK ADD FOREIGN KEY([member_id])
REFERENCES [dbo].[member] ([id])
ON UPDATE CASCADE
GO
ALTER TABLE [dbo].[club]  WITH CHECK ADD FOREIGN KEY([founder])
REFERENCES [dbo].[member] ([id])
ON UPDATE CASCADE
GO
ALTER TABLE [dbo].[club_event]  WITH CHECK ADD FOREIGN KEY([club_id])
REFERENCES [dbo].[club] ([id])
ON UPDATE CASCADE
GO
ALTER TABLE [dbo].[member]  WITH CHECK ADD FOREIGN KEY([gender_id])
REFERENCES [dbo].[gender] ([id])
ON UPDATE CASCADE
GO
ALTER TABLE [dbo].[member]  WITH CHECK ADD FOREIGN KEY([location_id])
REFERENCES [dbo].[member_location] ([id])
ON UPDATE CASCADE
GO
ALTER TABLE [dbo].[member_club]  WITH CHECK ADD FOREIGN KEY([club_id])
REFERENCES [dbo].[club] ([id])
GO
ALTER TABLE [dbo].[member_club]  WITH CHECK ADD FOREIGN KEY([member_id])
REFERENCES [dbo].[member] ([id])
GO
ALTER TABLE [dbo].[member_club]  WITH CHECK ADD FOREIGN KEY([role_type])
REFERENCES [dbo].[member_club_role] ([id])
GO
ALTER TABLE [dbo].[member_relation]  WITH CHECK ADD FOREIGN KEY([member_id1])
REFERENCES [dbo].[member] ([id])
GO
ALTER TABLE [dbo].[member_relation]  WITH CHECK ADD FOREIGN KEY([member_id2])
REFERENCES [dbo].[member] ([id])
GO
ALTER TABLE [dbo].[member_relation]  WITH CHECK ADD FOREIGN KEY([state_id])
REFERENCES [dbo].[relation_state] ([id])
GO
ALTER TABLE [dbo].[message]  WITH CHECK ADD FOREIGN KEY([receiver_id])
REFERENCES [dbo].[member] ([id])
GO
ALTER TABLE [dbo].[message]  WITH CHECK ADD FOREIGN KEY([sender_id])
REFERENCES [dbo].[member] ([id])
GO
ALTER TABLE [dbo].[message]  WITH CHECK ADD FOREIGN KEY([state_id])
REFERENCES [dbo].[message_state] ([id])
GO
ALTER TABLE [dbo].[model]  WITH CHECK ADD FOREIGN KEY([id_brand])
REFERENCES [dbo].[brand] ([id])
GO
ALTER TABLE [dbo].[post]  WITH CHECK ADD FOREIGN KEY([member_id])
REFERENCES [dbo].[member] ([id])
GO
ALTER TABLE [dbo].[post]  WITH CHECK ADD FOREIGN KEY([post_type])
REFERENCES [dbo].[post_type] ([id])
GO
ALTER TABLE [dbo].[post]  WITH CHECK ADD FOREIGN KEY([route_id])
REFERENCES [dbo].[route] ([id])
GO
ALTER TABLE [dbo].[post_comment]  WITH CHECK ADD FOREIGN KEY([post_id])
REFERENCES [dbo].[post] ([id])
ON UPDATE CASCADE
GO
ALTER TABLE [dbo].[route]  WITH CHECK ADD FOREIGN KEY([member_id])
REFERENCES [dbo].[member] ([id])
ON UPDATE CASCADE
GO
ALTER TABLE [dbo].[route]  WITH CHECK ADD FOREIGN KEY([terrain_type_id])
REFERENCES [dbo].[terrain_type] ([id])
ON UPDATE CASCADE
GO
USE [master]
GO
ALTER DATABASE [revup] SET  READ_WRITE 
GO
