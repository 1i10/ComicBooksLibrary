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

<img src="https://github.com/1i10/ComicBooksLibrary/blob/master/DataBaseModel/DataBaseModel.png" title="Физическая модель базы данных для хранения информации о комиксах"/>  

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
В роли источника для сбора информации о комиксах был выбран сайт [goodreads.com](https://www.goodreads.com/). И, поскольку, данный сайт сейчас имеет проблемы с отображением изображений, то для их парсинга будет использоваться сайт [comixology.com](https://www.comixology.com).  
  
Ниже будут представлены маски и теги кода страниц, которые содержат необходимую информацию.  
  
*1. Домен для перехода к странице комикса*  
> ```
> “https://www.goodreads.com/search?page=” + numberPage + “&q=” + newSearchLine + “&search_type=books”
>
> ```
>, где: numberPage - номер страницы, newSearchLine - это преобразованная строка с наименованием для вставки в URL (все пробелы заменяются на символ ‘+’).  
  
*2. Поиск ссылок с описанием комикса*  
```html
<a class="bookTitle" itemprop="url" href="ссылка на комикс">
```  
  
*3. Вытягивание информации о комиксе с найденной страницы*  
* *Поиск имени и url изображения обложки комикса*  
```html
... <img id="coverImage" alt="Наименование комикса" src="ссылка на изображение обложки комикса" /></a>
```  
* *Поиск всех авторов комикса*  
```html
<a class="authorName" itemprop="url" href="ссылка на автора"><span itemprop="name">Имя автора</span></a> ...
```  
* *Поиск описания комикса*  
```html
<span id="freeText(некий числовой идентификатор)" style="display:none">Расширенное описание комикса</span>
```  
* *Поиск включаемых номеров*
```html
<a href="/series/часть ссылки">Номер какого-то комикса</a>
```
* *Поиск даты публикации*  
```html
<div class="row">
    		 Информация о публикации
     		</div>

```

<a name="AppStructure"><h2>Общая структура приложения</h2></a>
Ниже будет представлена структура приложения с описаниями ее составляющих классов.  
  
*Описание структуры приложения*  
**Пакет** | **Наименование класса** | **Описание** |
:-------- | :---------------------- | :----------- |
[Model](https://github.com/1i10/ComicBooksLibrary/tree/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary/Model) | [Author](https://github.com/1i10/ComicBooksLibrary/blob/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary/Model/Author.java) | Содержит модель таблицы «Автор» |
[Model](https://github.com/1i10/ComicBooksLibrary/tree/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary/Model) | [CollectingComicNumbers](https://github.com/1i10/ComicBooksLibrary/blob/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary/Model/CollectingComicNumbers.java) | Содержит модель таблицы «Включаемые номера» |
[Model](https://github.com/1i10/ComicBooksLibrary/tree/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary/Model) | [ComicBook](https://github.com/1i10/ComicBooksLibrary/blob/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary/Model/ComicBook.java) | Содержит модель таблицы «Русскоязычное издание комикса» |
[Model](https://github.com/1i10/ComicBooksLibrary/tree/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary/Model) | [IncludedComicBook](https://github.com/1i10/ComicBooksLibrary/blob/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary/Model/IncludedComicBook.java) | Содержит модель таблицы «Включаемое иностранное издание» |
[-](https://github.com/1i10/ComicBooksLibrary/tree/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary) | [MainActivity](https://github.com/1i10/ComicBooksLibrary/blob/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary/MainActivity.java) | Основная активность, обеспечивающая переход по пунктам меню |
[-](https://github.com/1i10/ComicBooksLibrary/tree/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary) | [ImageSaveAndLoad](https://github.com/1i10/ComicBooksLibrary/blob/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary/ImageSaveAndLoad.java) | Класс для выполнения загрузки и сохранение изображений обложек в локальном хранилище устройства |
[-](https://github.com/1i10/ComicBooksLibrary/tree/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary) | [StringConverter](https://github.com/1i10/ComicBooksLibrary/blob/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary/StringConverter.java) | Вспомогательный класс для конвертирования списков авторов и включаемых номеров |
[MenuFragments](https://github.com/1i10/ComicBooksLibrary/tree/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary/MenuFragments) | [FirstFragment](https://github.com/1i10/ComicBooksLibrary/blob/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary/MenuFragments/FirstFragment.java) | Фрагмент меню, содержащий сохраненный список комиксов |
[MenuFragments](https://github.com/1i10/ComicBooksLibrary/tree/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary/MenuFragments) | [SecondFragment](https://github.com/1i10/ComicBooksLibrary/blob/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary/MenuFragments/SecondFragment.java) | Фрагмент меню, предназначенный для автоматизированного поиска иностранных комиксов |
[MenuFragments](https://github.com/1i10/ComicBooksLibrary/tree/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary/MenuFragments) | [ThirdFragment](https://github.com/1i10/ComicBooksLibrary/blob/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary/MenuFragments/ThirdFragment.java) | Фрагмент, который содержит информацию об приложении |
[ExpandableList](https://github.com/1i10/ComicBooksLibrary/tree/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary/MenuFragments/ExpandableList) | [ExpandableListAdapter](https://github.com/1i10/ComicBooksLibrary/blob/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary/MenuFragments/ExpandableList/ExpandableListAdapter.java) | Адаптер для отображения списка и его расширяющихся элементов |
[ExpandableList](https://github.com/1i10/ComicBooksLibrary/tree/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary/MenuFragments/ExpandableList) | [ModelItemView](https://github.com/1i10/ComicBooksLibrary/blob/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary/MenuFragments/ExpandableList/ModelItemView.java) | Класс, содержащий данные для включаемого иностранного издания |
[ExpandableList](https://github.com/1i10/ComicBooksLibrary/tree/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary/MenuFragments/ExpandableList) | [UpdatePosition](https://github.com/1i10/ComicBooksLibrary/blob/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary/MenuFragments/ExpandableList/UpdatePosition.java) | Активность, предназначенная для обновления текущей позиции |
[ListActions](https://github.com/1i10/ComicBooksLibrary/tree/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary/MenuFragments/ListActions) | [AddPosition](https://github.com/1i10/ComicBooksLibrary/blob/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary/MenuFragments/ListActions/AddPosition.java) | Активность, предназначенная для ручного ввода информации о комиксе |
[ListActions](https://github.com/1i10/ComicBooksLibrary/tree/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary/MenuFragments/ListActions) | [CollectViewAdapter](https://github.com/1i10/ComicBooksLibrary/blob/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary/MenuFragments/ListActions/CollectViewAdapter.java) | Адаптер для отображения списка включаемых иностранных изданий в активности AddPosition |
[ListActions](https://github.com/1i10/ComicBooksLibrary/tree/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary/MenuFragments/ListActions) | [ModelCollectView](https://github.com/1i10/ComicBooksLibrary/blob/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary/MenuFragments/ListActions/ModelCollectView.java) | Модель для передачи иностранного издания в адаптер |
[SearchMenu](https://github.com/1i10/ComicBooksLibrary/tree/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary/MenuFragments/SearchMenu) | [SearchListAdapter](https://github.com/1i10/ComicBooksLibrary/blob/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary/MenuFragments/SearchMenu/SearchListAdapter.java) | Адаптер для отображения найденных иностранных изданий через поиск для последующего добавления выбранных в список |
[Jsoup](https://github.com/1i10/ComicBooksLibrary/tree/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary/Jsoup) | [ParseBookInfo](https://github.com/1i10/ComicBooksLibrary/blob/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary/Jsoup/ParseBookInfo.java) | Класс для автоматизированного сбора информации о всех комиксах по введенному названию в строке поиска |
[DB](https://github.com/1i10/ComicBooksLibrary/tree/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary/DB) | [DBHandler](https://github.com/1i10/ComicBooksLibrary/blob/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary/DB/DBHandler.java) | Класс, который предоставляет работу с базой данный SQLite с созданием таблиц и запросами на добавление, редактирование, удаление, получение данных |
[DB](https://github.com/1i10/ComicBooksLibrary/tree/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary/DB) | [IncBookAddInDB](https://github.com/1i10/ComicBooksLibrary/blob/master/ComicsLibrary/app/src/main/java/com/app/comicslibrary/DB/IncBookAddInDB.java) | Вспомогательный класс для разбора ModelCollectView и добавления иностранного издания в базу данных | 


<a name="AppInterface"><h2>Разработка интерфейса приложения</h2></a>
<a name="AppStructure"><h2>Сборка и настройка</h2></a>
<a name="Example"><h2>Пример</h2></a>
<a name="NeedFix"><h2>Необходимо исправить</h2></a>

