For years, I have been deeply indebted to the generosity of others who 
have taken the time to document their technical learnings in one form or
another on the Internet. In that spirit I have created this blog, and I 
hope in time to generate content that creates some value. Eventually, there
will be a comment box following each post. Until then, use email if you wish
to comment. I am currently working mostly with the [Scala][9] programming 
language. 

This is the first time I have managed to set up a blog. The remainder of
this post will cover the path taken.

-------------------------------------------------------------------------------

## Starting Point

I made a prior effort in 2015 using [Pelican][0], the top open source Python
static site generator per [StaticGen][1]. Before 2016 I was doing most of my 
programming in Python, so it was a logical choice. Pelican supports a large 
number of [Themes][2] and I chose to work with the pelican-bootstrap3 theme.
I came close to getting the blog up and running, but never posted it.

## Evolution = transcend and include

Fast forward to 2017. I have been working primarily with Scala since early
2016. Revisiting StaticGen I found only three Scala static site generators 
listed. I was looking for something "dead simple", so of the two choices 
presented I decided to try [s2gen][3]. It worked as advertised, but I 
decided to try yet another direction after reading and working through
[Scala Scripting and the 15 Minute Blog Engine][4]. 

The approach presented in the above post used [Ammonite][5] and [ScalaTags][6]
to create a static site generator that did not involve a template language.
I was attracted by the thought of doing everything with Scala. Per the ScalaTags 
documentation:
>"leveraging the functionality of the Scala language to do almost everything, 
meaning you don't need to learn a second template pseudo-language just to stitch 
your HTML fragments together"

So borrowing heavily from [Haoyi's Programming Blog][7], I used Ammonite 
and ScalaTags, combined with the [Bootswatch Yeti][8] theme to create my own
dead simple static site generator.

## Further

One thing I like about this approach is the ease of making revisions and 
additions. Over time, I hope to make some additions to my dead simple blog: 
comments on posts, links to other scala blogs, links to social media, etc., perhaps
even creating another listed static site generator that uses Scala. 

-------------------------------------------------------------------------------



[0]: https://blog.getpelican.com/
[1]: https://www.staticgen.com/
[2]: https://github.com/getpelican/pelican-themes
[3]: http://appliedscala.com/s2gen/
[4]: http://www.lihaoyi.com/post/ScalaScriptingandthe15MinuteBlogEngine.html
[5]: http://lihaoyi.github.io/Ammonite/
[6]: http://lihaoyi.github.io/scalatags/
[7]: https://github.com/lihaoyi/blog
[8]: http://bootswatch.com/yeti/
[9]: https://www.scala-lang.org/