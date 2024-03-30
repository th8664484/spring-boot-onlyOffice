/**
 *
 * (c) Copyright Ascensio System SIA 2020
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
(function(window, undefined){

	window.Asc.plugin.init = function() {
		console.log("插件初始化")

		// none
		window.parent.Common.Gateway.on('internalcommand', function(data){
			console.log(data); // 接受到123参数console.log(data.data); // 接受到321参数
			var data_ = data.data
			switch (data.command){
				case "insertText":
					let text = "${"+data_.text+"}"
					window.Asc.plugin.executeMethod ("PasteText", [text]);
					if(window.Asc.plugin.info.editorType == 'cell'){
						CorrectText('insertText')
					}
					break;
				case 'getText':
					CorrectText('getText')
					break
			}
		});

		var _info = window.Asc.plugin.info;
		var userName =_info.userName
		var userId = _info.userId

		function CorrectText (command) {
			let data = {
				command:command,
				text:""
			}
			switch (window.Asc.plugin.info.editorType) {
				case 'word':
				case 'slide': {
					window.Asc.plugin.executeMethod ("GetSelectedText", [{"Numbering": false, "Math": false, "TableCellSeparator": '\n', "ParaSeparator": '\n', "TabSymbol": String.fromCharCode(9)}], function (e) {
						let text = e.trim()
						data.text = text
						window.parent.parent.postMessage(JSON.stringify(data),'*');
					});
					break;
				}
				case 'cell': {
					Asc.scope.command = command;
					window.Asc.plugin.callCommand (function () {
						var oWorksheet = Api.GetActiveSheet();
						var oActiveCell = oWorksheet.GetActiveCell();
						let str = oActiveCell.Text
						if(str.indexOf("${") != -1){
							str = str.substring(2,str.length-1)
						}
						let data = {
							command:Asc.scope.command,
							sheetName:oWorksheet.Name,
							text:str,
							x:oActiveCell.Row,
							y:oActiveCell.Col
						}

						localStorage.setItem('dataInfo', JSON.stringify(data))
					}, false,false,function(){
						window.parent.parent.postMessage(localStorage.getItem('dataInfo'),'*');
						localStorage.removeItem('dataInfo')
					});
					break;
				}
			}
		}
	};

	window.Asc.plugin.button = function(id) {
	};

})(window, undefined);
