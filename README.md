# Commercial template app

![screen shots](screenshots.png)

- reactive state driven
- multi-module
- clean
- UDF
- compose UI
- apollo (for graphQL calls)
- ktor (for rest calls)
- sqldelight (for db access)

The Wiki has a lot of the reasoning behind the various decisions made, but it's still a work in progress:

- [Module Structure](https://github.com/erdo/commercial-template/wiki/1-Module-Structure)
- [Main Pattern](https://github.com/erdo/commercial-template/wiki/2-Main-Pattern)
- [UI Hierarchy](https://github.com/erdo/commercial-template/wiki/3-UI-Hierarchy)
- [State vs Events](https://github.com/erdo/commercial-template/wiki/4-States-vs-Events)
- [Startup Process](https://github.com/erdo/commercial-template/wiki/5-Startup-Process)
- [Code Allocation](https://github.com/erdo/commercial-template/wiki/6-Code-Allocation)
- [Memory Performance](https://github.com/erdo/commercial-template/wiki/7-Memory-Performance)

## App template
In case you want to use this app as a starting point for something else, there is a bash script included that will rename the app packages (it's written for mac, use at your own risk).

```
git clone git@github.com:erdo/commercial-template.git
cd commercial-template
chmod u+x change_package.sh
./change_package.sh -p com.mydomain.myapp
```
Then open the app as usual in AS. You'll have to change the readme & the app icon yourself. And logcat can be filtered with: myapp_ (if you don't run the change_package script, logcat can be filtered with: clean_)

This repo is also setup as a **github template repository** so you might want to run the script after selecting "Use this template" on the github UI first (of course you'll have to change ":erdo/commercial-template.git" to whatever you called your new repo).

If you want to submit a PR though, you'll need to fork the repo, not template it.

# License

    Copyright 2015-2023 early.co

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
