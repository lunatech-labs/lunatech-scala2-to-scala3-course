# Node QUnit Puppeteer Plugin

[![NPM](https://nodei.co/npm/node-qunit-puppeteer.png?compact=true)](https://nodei.co/npm/node-qunit-puppeteer/)

> A simple node module for running qunit tests with [headless Chromium](https://github.com/GoogleChrome/puppeteer).

There is a common issue with PhantomJS failing with ES6 code, and the logical solution is to use Chrome Puppeteer instead.

## Usage

### Command-line utility

```
npm install -g node-qunit-puppeteer
node-qunit-puppeteer <URL> [<timeout>] [<puppeteerArgs>]
```

- `<URL>` - the address (or filepath) of the qunit HTML test page.
- `<timeout>` - (optional) test run timeout in milliseconds. Default is 30000.
- `<puppeteerArgs>` - (optional) Chrome command-line arguments. Default is "--allow-file-access-from-files".

#### Examples

`node-qunit-puppeteer https://example.org/ 10000 "--allow-file-access-from-files --no-sandbox"`
`node-qunit-puppeteer ./test/test-runner.html 10000 "--allow-file-access-from-files --no-sandbox"`

### Node module

- npm: `npm install node-qunit-puppeteer --save-dev`
- yarn: `yarn add node-qunit-puppeteer --dev`

#### Exported functions

- `async function runQunitPuppeteer(qunitPuppeteerArgs)` -- Opens the specified HTML page in a Chromium puppeteer and captures results of a test run. Returns an object with information on every module/test run.
- `function printOutput(qunitResult, console)` -- Takes the output of runQunitPuppeteer and prints it to console with identation and colors.
- `function printResultSummary(qunitResult, console)` -- Takes the output of runQunitPuppeteer and prints a summary to console with identation and colors.
- `function printFailedTests(qunitResult, console)` -- Takes the output of runQunitPuppeteer and prints failed test(s) information to console with identation and colors.
- `async function runQunitWithBrowser(browser, qunitPuppeteerArgs)` -- Runs the specified HTML page in the `puppeteer.Browser` instance. You might want to use this if you need to use a different puppeteer version or if you need to apply some additional logic on top of it.
- `async function runQunitWithPage(page, qunitPuppeteerArgs)` -- Runs the specified HTML page in the `puppeteer.Page` instance. Just like the previous function, you may want to use it if you need to use a different puppeteer version.

#### Examples

```javascript
const path = require("path");
const { runQunitPuppeteer, printOutput } = require("node-qunit-puppeteer");

const qunitArgs = {
  // Path to qunit tests suite
  targetUrl: `file://${path.join(__dirname, "tests.html")}`,
  // (optional, 30000 by default) global timeout for the tests suite
  timeout: 10000,
  // (optional, false by default) should the browser console be redirected or not
  redirectConsole: true,
  // (optional, ['--allow-file-access-from-files'] by default) Chrome command-line arguments
  puppeteerArgs: ["--allow-file-access-from-files"]
};

runQunitPuppeteer(qunitArgs)
  .then(result => {
    // Print the test result to the output
    printOutput(result, console);
    if (result.stats.failed > 0) {
      // Handle the failed test run
    }
  })
  .catch(ex => {
    console.error(ex);
  });
```

```javascript
const path = require("path");
const {
  runQunitPuppeteer,
  printResultSummary,
  printFailedTests
} = require("node-qunit-puppeteer");

const qunitArgs = {
  // Path to qunit tests suite
  targetUrl: `file://${path.join(__dirname, "tests.html")}`,
  // (optional, 30000 by default) global timeout for the tests suite
  timeout: 10000,
  // (optional, false by default) should the browser console be redirected or not
  redirectConsole: true
};

runQunitPuppeteer(qunitArgs)
  .then(result => {
    printResultSummary(result, console);

    if (result.stats.failed > 0) {
      printFailedTests(result, console);
      // other action(s) on failed tests
    }
  })
  .catch(ex => {
    console.error(ex);
  });
```

## Output

<img width="499" style="border: 1px solid #efefef" src="https://user-images.githubusercontent.com/5947035/47224776-0888c800-d3c5-11e8-9364-6d6f1d4b3bd1.png">

## `result` object

```json
{
  "modules": {
    "module 1": {
      "name": "module 1",
      "tests": [
        {
          "name": "module 1 simple test 1",
          "testId": "49b931ed",
          "skip": false,
          "module": "module 1",
          "previousFailure": false,
          "log": [
            {
              "module": "module 1",
              "name": "module 1 simple test 1",
              "result": true,
              "message": "Passed 1!",
              "actual": true,
              "testId": "49b931ed",
              "negative": false,
              "runtime": 1,
              "todo": false,
              "expected": true
            },
            {
              "module": "module 1",
              "name": "module 1 simple test 1",
              "result": true,
              "message": "Passed 2!",
              "actual": true,
              "testId": "49b931ed",
              "negative": false,
              "runtime": 2,
              "todo": false,
              "expected": true
            }
          ],
          "skipped": false,
          "todo": false,
          "failed": 0,
          "passed": 2,
          "total": 2,
          "runtime": 3,
          "assertions": [
            {
              "result": true,
              "message": "Passed 1!"
            },
            {
              "result": true,
              "message": "Passed 2!"
            }
          ],
          "source": "    at file:///Users/..../test-runner.html:17:11"
        }
      ],
      "failed": 0,
      "passed": 2,
      "runtime": 3,
      "total": 2
    },
    "module 2": {
      "name": "module 2",
      "tests": [
        {
          "name": "module 2 simple test 2",
          "testId": "9f962b0e",
          "skip": false,
          "module": "module 2",
          "previousFailure": false,
          "log": [
            {
              "module": "module 2",
              "name": "module 2 simple test 2",
              "result": true,
              "message": "Passed 1!",
              "actual": true,
              "testId": "9f962b0e",
              "negative": false,
              "runtime": 0,
              "todo": false,
              "expected": true
            }
          ],
          "skipped": false,
          "todo": false,
          "failed": 0,
          "passed": 1,
          "total": 1,
          "runtime": 0,
          "assertions": [
            {
              "result": true,
              "message": "Passed 1!"
            }
          ],
          "source": "    at file:///Users/..../test-runner.html:23:11"
        },
        {
          "name": "module 2 failed test 1",
          "testId": "aae8e622",
          "skip": false,
          "module": "module 2",
          "previousFailure": false,
          "log": [
            {
              "module": "module 2",
              "name": "module 2 failed test 1",
              "result": false,
              "message": "Passed 1!",
              "actual": false,
              "testId": "aae8e622",
              "negative": false,
              "runtime": 0,
              "todo": false,
              "expected": true,
              "source": "    at Object.<anonymous> (file:///Users/..../test-runner.html:28:14)"
            }
          ],
          "skipped": false,
          "todo": false,
          "failed": 1,
          "passed": 0,
          "total": 1,
          "runtime": 1,
          "assertions": [
            {
              "result": false,
              "message": "Passed 1!"
            }
          ],
          "source": "    at file:///Users/..../test-runner.html:27:11"
        }
      ],
      "failed": 1,
      "passed": 1,
      "runtime": 2,
      "total": 2
    }
  },
  "totalTests": 3,
  "stats": {
    "passed": 3,
    "failed": 1,
    "total": 4,
    "runtime": 35
  }
}
```
