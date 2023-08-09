/*
  @license
	Rollup.js v2.48.0
	Sat, 15 May 2021 04:50:11 GMT - commit 07b3a02069594147665daa95d3fa3e041a82b2d0


	https://github.com/rollup/rollup

	Released under the MIT License.
*/
'use strict';

var loadConfigFile_js = require('./shared/loadConfigFile.js');
require('fs');
require('path');
require('url');
require('./shared/rollup.js');
require('./shared/mergeOptions.js');
require('crypto');
require('events');



module.exports = loadConfigFile_js.loadAndParseConfigFile;
//# sourceMappingURL=loadConfigFile.js.map
