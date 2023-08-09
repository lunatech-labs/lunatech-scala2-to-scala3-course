# babel-plugin-transform-html-import-to-string

Turn HTML imports into strings.

## Example

Given the following _example.html_.

```html
<h1>Hello</h1>
```

#### in

```js
import html from './example.html';
```

#### out

```js
var html = '<h1>Hello</h1>';
```


## Installation

```sh
$ npm install babel-plugin-transform-html-import-to-string
```

## Usage

### Via `.babelrc` (Recommended)

**.babelrc**

```json
{
  "plugins": ["transform-html-import-to-string"]
}
```

### Via CLI

```sh
$ babel --plugins transform-html-import-to-string script.js
```

### Via Node API

```javascript
require("babel-core").transform("code", {
  plugins: ["transform-html-import-to-string"]
});
```