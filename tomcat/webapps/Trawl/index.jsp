<%@page import="search.trawl.BasicSearchResult"%>
<%@page import="search.trawl.BasicSearch"%>
<%@page import="data.biotree.TaxonNode"%>
<%@page import="data.biotree.BioTree"%>
<%@page import="data.DataStore"%>
<%@page import="java.io.FileInputStream" %>
<%@page import="java.io.ObjectInputStream" %>
<%@page import="java.io.FileNotFoundException" %>
<%@page import="data.Record"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="keywords" content="insert, some, keywords"> <!--TODO-->
    <meta name="description" content="insert a description"> <!--TODO-->
    <title>TrawlExpert</title>
    <link rel="stylesheet" type="text/css" href="style.css">

    <!--JQuery-->
    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <script src="https://cdn.plot.ly/plotly-latest.min.js"></script>
    <script src="histogram.js"></script>
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBmfzgAfA1sXItGVdGiC5txAieu8VYXcZM&libraries=visualization"></script>

    <script src="map.js"></script>
    <script src="infoWindow.js"></script>
    <script src="infoGenerator.js"></script>
    
    <script src="cluster.js"></script>
   

    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">

    <!-- MUST load JQuery Library before loading this-->
    <script src="script.js"></script>

    <!--Fonts-->
    <!--Open Sans Rg-400/Semi-600/Bd-700-->
    <link href="https://fonts.googleapis.com/css?family=Open+Sans:300,400,600,700" rel="stylesheet">

    <!--Plugins-->
</head>
<body>
    <header>
        <div class="headerWrapper">
            <a href="index.jsp" >TrawlExpert</a>
            <span class="nav-bar">
                <a href="about.html">About</a> |
                <a href="doc/" target="_blank">TrawlExpert API</a> |
                <a href="designSpec.pdf" target="_blank">Design Report</a> |
                <a href="https://gitlab.cas.mcmaster.ca/schankuc/2XB3" target="_blank">Git</a>
            </span>
        </div>
    </header>

    <section id="main">
        <section id="formWrapper">
            <section id="pickSciRanks">
                Phylum:
                <select name="pickSciR" id="pickPhylum" size="1" onChange="callUpdateSci(this)"><!--Dynamically Filled--></select>
                Class:
                <select name="pickSciR" id="pickClass" size="1" onChange="callUpdateSci(this)"><%--<option value="00">Arthropoda</option>--%></select>
                Order:
                <select name="pickSciR" id="pickOrder" size="1" onChange="callUpdateSci(this)"><!--Dynamically Filled--></select>
                Family:
                <select name="pickSciR" id="pickFamily" size="1" onChange="callUpdateSci(this)"><!--Dynamically Filled--></select>
                Genus:
                <select name="pickSciR" id="pickGenus" size="1" onChange="callUpdateSci(this)"><!--Dynamically Filled--></select>
                Species:
                <select name="pickSciR" id="pickSpecies" size="1"><!--Dynamically Filled--></select>
            </section>

            <section id="yearIn">
                Year Range:
                <span id="fromtoYear"></span>
                <form>
                    <div id="slider-range"></div>
                </form>
            </section>

            <section id="outputIn">
                <%--Map and Histogram must have the same name for default checkbox to function correctly--%>
                Cluster Size:
                <input type="number" id="pickSize" >km^2
                <input type="radio" name="pickOutput" value="cluster" > Cluster
                <input type="radio" name="pickOutput" value="map"> Map
                <input type="radio" name="pickOutput" value="heat"> Heatmap
                <input type="radio" name="pickOutput" value="histogram" checked> Histogram
                <input type="radio" name="pickOutput" value="list" checked> List
                <button type="button" onClick="callOutput()">Load</button>
            </section>
        </section>


        <section id="outputWrapper">
            <section id="outputDetails">
                <!--  Stuff like population count, entries found, etc. go here -->
            </section>

            <section id="outputBox"><!--  Map, Histogram Box. Histogram selected by default but we might want to have a loading screen instead.-->
                <%--<div id="histogram"></div>--%>
                <%--<div id="map"></div>--%>
                <%--<div id="heat"></div>--%>
                <%--<div id="cluster"></div>--%>
                <%--<div id="console">~~~ PSEUDO-CONSOLE ~~~<br></div>--%>
            </section>
        </section>
    </section>
</body>
</html>