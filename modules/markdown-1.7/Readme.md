#Markdown module for the play framework

##What is markdown ?

Markdown is a text-to-HTML filter; it translates an easy-to-read / easy-to-write structured text 
format into HTML. Markdown's text format is most similar to that of plain text email, and supports 
features such as headers, *emphasis*, code blocks, blockquotes, and links.

##What does the module provide ?

The module provides 3 integration points:

1. First, a groovy page java extension ${str.markdown().raw()}
2. A basic controller to quickly serve your markdown contents, extend to your likings.
3. Last but not least, a powerful developer API with support for streams and markdown document trees. 

##How is this markdown module better than others ?

The module is based on the excellent [markdownPapers](https://github.com/lruiz/MarkdownPapers) project which not only shines by its speed (4x faster than the nearest competitor), but also by its design:

Based on JavaCC, the markdown parser can work with streams and build a markdown document tree. The tree can be processed using a Visitor pattern. It's a great way to enhance the markdown grammar with your own extensions.

Overall, MarkdownPaper is faster and uses less memory than any of its Java competitors.

##Usage:

### Groovy page

Probably the easiest way to add markdown contents on a page. You can type
the markdown text or read it from a file:

    ${'**Hello** *world*'.markdown().raw()}
    ${'./manual.md'.markdown().raw()}

Or, if you want, use the dedicated functions `markdownText()` and 
`markdownFile()`.

### Controller

Add the following to the conf/route

    GET     /docs/images/{imageName}\.{ext}     MarkdownController.image
    GET     /docs/{page}\.md                    MarkdownController.transform

Ensure the markdown pages are located in

*. public/mddocs/*.md     -for the documents
*. public/mddocs/images/  -for the images

Now access the page using `http://localhost:9000/docs/intro.md`

PS: You may extend the controller and implement new actions that better fit your preferences.

### API

The utility class `markdown.Markdown` provide two generic methods:

    public static String transformMarkdown(String markdown)	throws java.text.ParseException;
    public static String transformMarkdown(Reader markdownReader) throws java.text.ParseException;	

Should you need to handle a markdown document tree by your own, you will need to use the native package **org.tautua.markdownpapers.Markdown**

    Reader in = new FileReader("in.md");
    Visitor v = new HtmlEmitter();
    Parser parser = new Parser(in);

    Document doc = parser.parse();
    doc.accept(v);

## Sample application

Part of the distribution

## Credits

markdownPapers - Larry Ruiz - [https://github.com/lruiz/MarkdownPapers](https://github.com/lruiz/MarkdownPapers)

play-markdown module - Olivier Refalo - [https://github.com/orefalo](https://github.com/orefalo)

## History

Version 1.7 : Added ability to read markdown files from groovy
              markdownPaper upgraded to v1.2.7
Version 1.6 : markdownPaper upgraded to v1.2.6
Version 1.5 : fixed: deploying in prod mode fails compilation
Version 1.4 : markdownPaper upgraded to v1.2.5
Version 1.3 : markdownPaper upgraded to v1.2.4
