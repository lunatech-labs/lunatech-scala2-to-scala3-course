#! /usr/bin/env node

const path = require('path');
const { runQunitPuppeteer, printOutput } = require('./index');

const Console = console;
const args = process.argv.slice(2);

if (args.length < 1 || args.length > 3) {
  Console.log('Usage: node-qunit-puppeteer <URL> [<timeout>] [<puppeteerArgs>]');
  process.exit(1);
}

let targetUrl = args[0];

if (targetUrl.indexOf('http://') === 0 || targetUrl.indexOf('https://') === 0) {
  // Absolute URL, do nothing
} else if (!path.isAbsolute(targetUrl)) {
  // Relative file path -- append file://
  targetUrl = `file://${path.join(process.cwd(), targetUrl)}`;
} else if (targetUrl.indexOf('file://') !== 0) {
  // Absoolute path -- prepent file://
  targetUrl = `file://${targetUrl}`;
}

const qunitArgs = {
  targetUrl,
  timeout: parseInt(args[1] || 30000, 10),
  redirectConsole: false,
};

Console.log(`Target URL is ${qunitArgs.targetUrl}, timeout is ${qunitArgs.timeout}`);

const puppeteerArgsStr = args[2];
if (typeof puppeteerArgsStr === 'string') {
  const puppeteerArgs = puppeteerArgsStr.split(/(\s+)/).filter((s) => s.trim().length > 0);
  if (args.length > 0) {
    qunitArgs.puppeteerArgs = puppeteerArgs;
    Console.log(`Puppeteer args are: ${puppeteerArgs.join(' ')}`);
  }
}

runQunitPuppeteer(qunitArgs)
  .then((result) => {
    // Print the test result to the output
    printOutput(result, Console);
    if (result.stats.failed > 0) {
      // Handle the failed test run
      process.exit(1);
    } else {
      process.exit(0);
    }
  })
  .catch((ex) => {
    Console.error(ex);
    process.exit(1);
  });
