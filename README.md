# gtbot

A Clojure library to translate text through the Google Translate web
interface.


## Usage

Add the dependency to your project file:

``` clojure
[fourtytoo/gtbot "0.1.0-SNAPSHOT"]
```

If gtbot project is _not_ listed on Clojars it means you'll have to
download it and install it yourself.  Here is how.

Clone the library from GitHub:
``` sh
cd somewhere/outside/your/project
git clone git@github.com:fourtytoo/gtbot.git
```	

Add the dependency to your project.  Check how to do it on the
leiningen [page](https://github.com/technomancy/leiningen/blob/stable/doc/TUTORIAL.md#checkout-dependencies). 
But that basically boils down to creating a checkouts directory in the
root of your project

``` sh
cd your/project/root/directory
mkdir checkouts
```

and inside it, creating a symbolic link to gtbot's root directory
``` sh
cd checkouts
ln -s somewhere/outside/your/project/gtbot .
```

Gtbot uses [http-kit](https://github.com/http-kit/http-kit ) to make
HTTP requests and [data.json](https://github.com/clojure/data.json )
to decode JSON.  If you are already using your own version of these
libraries you may want to exclude gtbot's own:

``` clojure
[fourtytoo/gtbot "0.1.0-SNAPSHOT" :exclusions [http-kit org.clojure/data.json]]
```

Add gtbot.core to your namespace:

``` clojure
(require [gtbot.core :refer [translate]])
```

Translate:

``` clojure
(translate "de" "en" "Kartoffel")
=> {:detailed [["noun" ["potato"] [["potato" ["Kartoffel" "Knolle"] nil 0.69811249]] "Kartoffel" 1]], :detailed-definition nil, :suggestion nil, :phonetic "", :translation "potato", :text-phonetic ""}
```


## Pros and Cons

This library uses the web interface of Google Translate.  The one
users see with their web browser.  That means it doesn't use the
official Google Translate API, which would be much simpler. As a bot
would it pretends to be a web browser, visits the Google Translate
page and replicates what its JavaScript does to produce the
translation for you.

There are already loads of libraries out there using both the official
REST API and/or this approach.  This one is implemented in Clojure.

While the Google REST API cannot be used for free, it is more stable
and reliable; it won't change under your nose overnight.  The REST API
is a bit trickier to set up, but once you get your authentication key,
it is admittedly a better choice.  If you intend to use the Google
Translate service for more than toy applications, educational or
occasional personal purposes, I'd recommend you use one of those
libraries which interface to the official REST API.  They are
available also for Clojure; search for "google tranlsate clojure" on
GitHub.

The libraries, such as this one, who rely on the reverse engineering
of the Google web page, may find themselves not working any more if
Google decides to change its page.  This is for free, but it won't
work for anything but interactive use; google is likely to blacklist
those IP address who exploit their page with a bot.


## Origin

The code in this library has been derived from
[google-translate.el](https://github.com/atykhonov/google-translate).
I don't think that was the first library of this kind, nor the last
one, it just was written in Emacs Lisp, which is to me less irritating
than JavaScript.  Not much effort has been put into understanding the
algorithm employed, beyond what could be obviously simplified.

If you really are interested in this aspect I must warn you that the
original code (google-translate.el) has little documentation and
itself is based on the reverse engineering of Google code, which is
obfuscated JavaScript.


## License

Copyright © 2017 Walter C. Pelissero

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
