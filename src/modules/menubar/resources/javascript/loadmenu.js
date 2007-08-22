/*
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements.  See the NOTICE file distributed with
* this work for additional information regarding copyright ownership.
* The ASF licenses this file to You under the Apache License, Version 2.0
* (the "License"); you may not use this file except in compliance with
* the License.  You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

dojo.require("dojo.logging.*");
dojo.require("dojo.event.*");
dojo.require("dojo.io.*");

function loadMenu(e) {
	var menuFunction = {
		url: MENU_URL + "&lenya.module=menubar",
		load: function(type, data, evt) {
			var placeholderElement = document.getElementById("lenyaMenuPlaceholder");
			var menuElement = document.getElementById("lenyaMenuContainer");
			menuElement.removeChild(placeholderElement);
			menuElement.appendChild(data.documentElement);
			initialize();
		},
		error: function(type, error) {
			dojo.log.error(error.message);
		},
		mimetype: "text/xml",
		method: "GET"
	};
	dojo.io.bind(menuFunction);
}

/*
* Customize this method for event-based loading.
*/
function initAjax() {
	dojo.event.connect(dojo.byId("dojotest"), "onclick", "loadMenu");
}

dojo.addOnLoad(loadMenu);
