// Initialization
function init() {
    console.log("trying to init");
    callUpdateSci({id:"pickAnimalia", value:2});    // Propagate Dropdowns on Startup
    reqHistogram(2);
}

function resolveAny(obj) {
    if(obj.id === "pickPhylum"){
        return {id:"pickAnimalia", value:2};
    }
    else {
        if (Number(obj.previousElementSibling.value) === -1) {
            return resolveAny(obj.previousElementSibling);
        }
        else {
            return {id: obj.previousElementSibling.id, value: obj.previousElementSibling.value};
        }
    }
}

// Updates the dropdown menus
// Called whenever a dropdown menu changes and on init
function callUpdateSci(object){
    var tagID = object.id;
    var taxID = Number(object.value);

    // Resolve "Any" option
    if (taxID === -1){
        var obj2 = resolveAny(object);
        tagID = obj2.id;
        taxID = Number(obj2.value);
    }
    reqBioLookup(tagID, taxID);
}

//  Populates a menu's innerHTML with options
function populateDropdown(childTagID, nodeList, subnode){
    var x, y, content="";
    // console.log(nodeList[subnode]);
    if(nodeList[subnode] != null){
        for (var i in nodeList[subnode]["taxonName"]){
            x = nodeList[subnode]["taxonId"][i];
            y = nodeList[subnode]["taxonName"][i];
            content += '"<option value="' + x + '">' + y + '</option>';
        }
    }
    document.getElementById(childTagID).innerHTML = content;
}

// Calls populate dropdown for all menus based off what has changed.
function updateSci(tagID, nodeList) {
    while (tagID !== "END"){
        switch (tagID){
            case "pickAnimalia":
                populateDropdown("pickPhylum", nodeList, "phylum");
                tagID = "pickPhylum";
                break;

            case "pickPhylum":
                populateDropdown("pickClass", nodeList, "class");
                tagID = "pickClass";
                break;

            case "pickClass":
                populateDropdown("pickOrder", nodeList, "order");
                tagID = "pickOrder";
                break;

            case "pickOrder":
                populateDropdown("pickFamily", nodeList, "family");
                tagID = "pickFamily";
                break;

            case "pickFamily":
                populateDropdown("pickGenus", nodeList, "genus");
                tagID = "pickGenus";
                break;

            case "pickGenus":
                populateDropdown("pickSpecies", nodeList, "species");
                tagID = "pickSpecies";
                break;

            case "pickSpecies":
                tagID = "END";
                break;
        }
    }
}

// This function will get the new nodelist from the server.
// ParentId is the taxId of the node that holds the list of children we want.
function reqBioLookup(tagID, parentId){
    var path = 'doBioLookup.do';
    var params = {taxId: Number(parentId)};
    var xhr = new XMLHttpRequest();
    xhr.open("POST", path);
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");  //Send the proper header info

    xhr.onreadystatechange = function() {//Call a function when the state changes (i.e. response comes back)
        // Update the dropdown when response is ready
        if(xhr.readyState == XMLHttpRequest.DONE && xhr.status == 200) {
            var nodeList = JSON.parse(this.responseText);
            updateSci(tagID, nodeList);
        }
        else{
            console.log("Server Response: Error"); //RME
        }
    };

    var jsonString= JSON.stringify(params);     //generate JSON string
    xhr.send(jsonString);                       //send request to server
    // document.getElementById("console").innerHTML += "Sent request to " + path + ": "  + jsonString + "<br>"; //RME
}

//This function will get the new nodelist from the server.
//ParentId is the taxId of the node that holds the list of children we want.
function reqHistogram(params){
     var path = 'doHist.do';
     var xhr = new XMLHttpRequest();
     xhr.open("POST", path);
     xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");  //Send the proper header info

     xhr.onreadystatechange = function() {//Call a function when the state changes (i.e. response comes back)
         // Update the dropdown when response is ready
         if (xhr.readyState == XMLHttpRequest.DONE && xhr.status == 200) {
             var nodeList = JSON.parse(this.responseText);
             histogram(nodeList["x"], nodeList["y"]);
             document.getElementById("outputDetails").innerHTML = "Found " + nodeList["n"] + " results " + "(" + nodeList["time"] + " seconds)<br> Total Population: " + nodeList["individualCount"];
         }
         else {
             console.log("Server Response: Error"); //RME
         }
     };
     xhr.send(params);                       //send request to server
     // document.getElementById("console").innerHTML += "Sent request to " + path + ": "  + jsonString + "<br>"; //RME
 }

 //Returns the most general taxon group (leftmost)
function getTaxGroup() {
    var tagID = "pickSpecies";
    var taxGroup = document.getElementById(tagID);

    while (true){
        console.log(taxGroup.value);
        if(taxGroup.value !== null && taxGroup.value !== "-1"){
            console.log("returning Animalia");
            return Number(taxGroup.value);
        }
        else if(tagID === "pickAnimalia"){
            console.log("in pick Animalia");
            return 2;
        }
        else{
            taxGroup = document.getElementById(tagID);
            console.log("Switching: " + tagID);
            switch (tagID){
                case "pickPhylum":
                    tagID = "pickAnimalia";
                    break;

                case "pickClass":
                    tagID = "pickPhylum";
                    break;

                case "pickOrder":
                    tagID = "pickClass";
                    break;

                case "pickFamily":
                    tagID = "pickOrder";
                    break;

                case "pickGenus":
                    tagID = "pickFamily";
                    break;

                case "pickSpecies":
                    tagID = "pickGenus";
                    break;
            }
        }
    }
}

// Requests info window map from the server
function reqMap(params){
    var path = 'doMap.do';
    var xhr = new XMLHttpRequest();
    xhr.open("POST", path);
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");  //Send the proper header info

    xhr.onreadystatechange = function() {//Call a function when the state changes (i.e. response comes back)
        // Update the dropdown when response is ready
        if (xhr.readyState == XMLHttpRequest.DONE && xhr.status == 200) {
            var nodeList = JSON.parse(this.responseText);
            console.log(nodeList);
            initInfo(nodeList["latitude"], nodeList["longitude"], nodeList["name"], nodeList["date"], nodeList["individualCount"]);
        }
        else {
            console.log("Server Response: Error"); //RME
        }
    };
    xhr.send(params);                       //send request to server
}

// Requests heatmap from the server
function reqHeat(params){
var path = 'doMap.do';
var xhr = new XMLHttpRequest();
xhr.open("POST", path);
xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");  //Send the proper header info

xhr.onreadystatechange = function() {//Call a function when the state changes (i.e. response comes back)
    // Update the dropdown when response is ready
    if (xhr.readyState == XMLHttpRequest.DONE && xhr.status == 200) {
        var nodeList = JSON.parse(this.responseText);
        initMap(nodeList["latitude"], nodeList["longitude"]);
    }
    else {
        console.log("Server Response: Error"); //RME
    }
};
xhr.send(params);                       //send request to server
}

// Requests cluster map from the server
function reqCluster(params){
    var path = 'doCluster.do';
    var xhr = new XMLHttpRequest();
    xhr.open("POST", path);
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");  //Send the proper header info

    xhr.onreadystatechange = function() {//Call a function when the state changes (i.e. response comes back)
        // Update the dropdown when response is ready
        if (xhr.readyState == XMLHttpRequest.DONE && xhr.status == 200) {
            var nodeList = JSON.parse(this.responseText);
            initCluster(nodeList["latitude"], nodeList["longitude"], nodeList["n"], nodeList["individualCount"]);
        }
        else {
            console.log("Server Response: Error"); //RME
        }
    };
    xhr.send(params);                       //send request to server
}

// Requests list of individual results from the server
function reqResults(params, lo, hi){
    var path = 'doList.do';
    var xhr = new XMLHttpRequest();
    xhr.open("POST", path);
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");  //Send the proper header info

    xhr.onreadystatechange = function() {//Call a function when the state changes (i.e. response comes back)
        // Update the dropdown when response is ready
        if (xhr.readyState == XMLHttpRequest.DONE && xhr.status == 200) {
            var respHtml = this.responseText;
            document.getElementById("list").innerHTML = respHtml;
        }
        else {
            console.log("Server Response: Error"); //RME
        }
    };
    params = JSON.parse(params);
    params["searchLo"] = lo;
    params["searchHi"] = hi;
    params = JSON.stringify(params);
    xhr.send(params);                       //send request to server
}

// Finds which output option is selected
// Creates JSON object from input form values
// Clears current innerHTML of output elements
// Calls the appropriate server request function
function callOutput(){
    var pickOutputType = document.getElementsByName('pickOutput');
    var outType, taxGroup, yearFrom, yearTo, clusterSize;
    // Get output selection: Map/Histogram
    for(var i = 0; i < pickOutputType.length; i++){
        if(pickOutputType[i].checked){
            console.log(pickOutputType[i].value);   //RME
            outType = pickOutputType[i].value;
        }
    }
    taxGroup = getTaxGroup();
    yearFrom = $( "#slider-range" ).slider( "values", 0 );
    yearTo = $( "#slider-range" ).slider( "values", 1 );
    clusterSize = document.getElementById("pickSize").value;
    console.log("Cs: " + clusterSize);
    var params= JSON.stringify({taxId: Number(taxGroup), yearF: Number(yearFrom), yearT: Number(yearTo), area: Number(clusterSize)});

    //Switch Output Display
    document.getElementById("outputBox").innerHTML = "";
    document.getElementById("outputDetails").innerHTML = "";

    // Find the type of output selected and call the respective function
    if(outType === "histogram"){
        document.getElementById("outputBox").innerHTML='<div id="histogram"></div>';
        reqHistogram(params);
    }
    else if(outType === "map"){
        document.getElementById("outputBox").innerHTML='<div id="map"></div>';
        reqMap(params);
    }
    else if(outType === "heat"){
        document.getElementById("outputBox").innerHTML='<div id="heat"></div>';
        reqHeat(params);
    }
    else if(outType === "cluster"){
        document.getElementById("outputBox").innerHTML='<div id="cluster"></div>';
        reqCluster(params);
    }
    else if(outType === "list"){
        document.getElementById("outputBox").innerHTML='<div id="list"></div>';
        reqResults(params, 0, 50);
    }
}

// JQuery Range Slider
// Used for year range input
$( function() {
    $( "#slider-range" ).slider({
        range: true,
        min: 1960,
        max: 2016,
        values: [ 1960, 2016 ],
        slide: function( event, ui ) {
            $( "#fromtoYear" ).html(ui.values[ 0 ] + " - " + ui.values[ 1 ] )
        }
    });
    $( "#fromtoYear" ).html($( "#slider-range" ).slider( "values", 0 ) + " - " + $( "#slider-range" ).slider( "values", 1 ))
} );

// When the window initializes, call init().
window.onload = init;