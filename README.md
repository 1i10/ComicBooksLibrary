## Навигация   
[1. Краткое описание](#Description)  
[2. Постановка задачи](#Task)  
-- [2.1. Функциональные требования](#FunctionalRequirements)  
-- [2.2. Нефункциональные требования](#NonFunctionalRequirements)  
[3. Проектирование базы данных](#DBdesign)  
-- [3.1 Физическая модель](#PhysModel)  
-- [3.2 Описание сущностей](#EntityDB)  
[4. Анализ кода страниц для автоматизированного сбора информации](#AnalysisHtmlCode)  
[5. Общая структура приложения](#AppStructure)  
[6. Разработка интерфейса приложения](#AppInterface)  
[7. Сборка и настройка](#Build)  
[8. Руководство пользователя](#UserGuide)  
[9. Пример](#Example)  
[10. Необходимо исправить](#NeedFix)  
  
<a name="Description"><h2>Краткое описание</h2></a>
Существует такая разновидность книг как комиксы. В России имеется некоторый ряд издательств, которые занимаются выпуском иностранных изданий на русском языке. Как правило, в одно русскоязычное издание помещается несколько иностранных. И, человек, который хочет найти информацию об конкретном включаемом комиксе, будет тратить на это много времени. Сначала ему необходимо будет ввести наименование комикса на английском языке, а после заниматься сопоставлением включаемых номеров, которые указаны на обложке. Все усугубляется для людей, которые занимаются коллекционированием и заносят эту и другую информацию о комиксах в некоторую базу данных вручную. Этим и вызвана актуальность разработки приложения для учета комиксов.  
  
Разрабатываемое приложение предназначено для решения следующих задач:
* Автоматизация сбора данных об иностранных комиксах, включаемых в русскоязычное издание;
* Создание базы данных для хранения информации;
* Обеспечение поиска информации;
* Оптимальное использование рабочего времени.  
  
Мобильное приложение будет разрабатываться для устройств Android в среде Android Studio.  
Выбранная SDK: Android 4.4 KitKat. Выбор обосновывается тем, что предполагается использование приложения как современными устройствами, так и старыми, чтобы охватить более широкий круг пользователей.  
Данные будут храниться локально на устройстве с помощью [SQLite](https://blog.skillfactory.ru/glossary/sqlite/).  
Для парсинга веб-страниц будет использована библиотека [JSoup](https://jsoup.org/).

<a name="Task"><h2>Постановка задачи</h2></a>
Необходимо разработать приложение, отвечающее следующим требованиям.
<a name="FunctionalRequirements"><h3>Функциональные требования</h3></a>
* Отображение списка русскоязычных комиксов и включаемых в них иностранные издания;
* Ручное добавление комикса в список;
* Автоматическое добавление комикса в список;
* Поиск включаемых комиксов для дальнейшего добавления в список;
* Редактирование позиции;
* Удаление позиции;
* Отображение информации о статусе прочтения и стоимости комикса.  

<a name="NonFunctionalRequirements"><h3>Нефункциональные требования</h3></a>
* Приложение должно иметь отдельное меню для списка и поиска комиксов для добавления;
* Поиск включаемых комиксов осуществляется только при наличии соединения с интернетом;
* В списке должны отображаться только основные комиксы (наименование, цена, статус), а список включаемых только внутри этой позиции.

<a name="DBdesign"><h2>Проектирование базы данных</h2></a>
<a name="PhysModel"><h3>Физическая модель</h3></a>

<img src="https://github.com/1i10/ComicsLibrary/blob/master/DataBaseModel/DataBaseModel.png" title="Физическая модель базы данных для хранения информации о комиксах"/>  

<a name="EntityDB"><h3>Описание сущностей</h3></a>
База данных включает в себя 4 таблицы: русскоязычное издание комикса (*Comic_book*), включаемое иностранное издание (*Included_comic_book*), автор (*Author*), включаемые номера комикса в иностранное издание (*Collecting_comic_numbers*).  
(*Идентификаторы первичных и внешних ключей не будут отражаться в таблицах, представленных ниже*)
  
*Данные русскоязычного издания комикса*
**Атрибут** | **Описание** | **Возможные значения** |
:---------- | :----------- | :--------------------- |
Name_book | Наименование русскоязычного издания комикса | Любой набор символов |
Status | Статус о прочтении комикса пользователем | Одна из строк: «Не прочитано», «Прочитано», «В процессе» |
Price | Стоимость комикса в рублях | Целое число |
  
*Включаемое иностранное издание*
**Атрибут** | **Описание** | **Возможные значения** |
:---------- | :----------- | :--------------------- |
Name_inc_book | Наименование включаемого иностранного издания комикса | Любой набор символов |
Description | Описание комикса | Любой набор символов |
Published_date | Дата публикации | Строка в дата формате |
Path_image | Путь к хранимой на устройстве обложке комикса | Строка, содержащая путь до директории с изображениями |
  
*Автор комикса*
**Атрибут** | **Описание** | **Возможные значения** |
:---------- | :----------- | :--------------------- |
First_name | Имя автора | Любой набор символов |
Last_name | Фамилия автора | Любой набор символов |
  
*Включаемые номера комикса*
**Атрибут** | **Описание** | **Возможные значения** |
:---------- | :----------- | :--------------------- |
Name_collect_book | Наименование включаемого номера комикса в иностранное издание | Любой набор символов |

<a name="AnalysisHtmlCode"><h2>Анализ кода страниц для автоматизированного сбора информации</h2></a>
<a name="AppStructure"><h2>Общая структура приложения</h2></a>
<a name="AppInterface"><h2>Разработка интерфейса приложения</h2></a>
<a name="AppStructure"><h2>Сборка и настройка</h2></a>
<a name="Example"><h2>Пример</h2></a>
<a name="NeedFix"><h2>Необходимо исправить</h2></a>

