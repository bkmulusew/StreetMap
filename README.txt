Name: Beakal Lemeneh
Net ID: 31390484
Project Number: 03

I did not collaborate with anyone in this project.


The code first reads through the whole file and stores all the intersections as vertices and all the roads as edges in different array lists. Then the minimum latitude value is deducted from all the latitude values and then all the latitude values are divided by the maximum latitude value in order to find the coordinates that could help fit the map into the screen. Same thing is also done for all the longitudes. That is what line 303-317 does on the code.

The classes I used to implement this project and their purposes are explained in detailed manner in the comment section of the code.

Using the Haversine formula, the distance of each road is calculated by taking the latitude and longitude of its starting and ending point. The distance is then used as a weight for the edges. Once, all the weights are calculated, all the edges are stored as a adjacency list, and the Dijkstra's algorithm is run to calculate the shortest distance in kms from a certain source to a certain destination, chosen by the user, and the path that needs to be taken to travel the shortest distance. Finally, based on the command entered by the user, the shortest distance in kms from a certain source to a certain destination, chosen by the user, and the path that needs to be taken to travel the shortest distance is both printed in the console and displayed on a map using Java Graphics. The path that needs to be taken to travel the shortest distance is shown using a thick red line.

One of the obstacles I faced in doing the project is that the Dijkstra's algorithm I wrote always puts in the first intersection in the text file as the starting point even when the source is a different intersection. The other one is that the Dijkstra algorithm shows no path from any point to the first intersection in the text file. I had to write 3 if-else conditions - lines 218-230 on the code - to mend these problems.

The code runs very fast for the ur.txt file, but it takes unto 5 min to compute the user's command for the monroe.txt file. It takes more that 10 min for the nys.txt file.
