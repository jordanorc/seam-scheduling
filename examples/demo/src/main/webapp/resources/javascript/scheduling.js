/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
seam = {}

seam.scheduling = {

    updateTextArea: function(data) {
        for(var jobName in data) {
            var obj=document.getElementById(jobName);
            var text = data[jobName].replace(/\n\s*/g, '<br />');
            obj.innerHTML += text;
            obj.scrollTop = obj.scrollHeight;
        }
    },

    clearLog: function(jobName) {
        var obj=document.getElementById(jobName);
        obj.innerHTML = "";
    }

}