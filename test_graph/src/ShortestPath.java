

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class ShortestPath {
	
	static final int V = 6;
	
	StringBuilder Print_func(List<Vertex> prev,Vertex [] vertices,int src)
	{
		//print all widest path
		System.out.println("Will print all possible widest path first");
		
		int i = 0;
		StringBuilder results = new StringBuilder();
		StringBuilder output = new StringBuilder();
		for(i=0;i<V;i++)
		{
			if(i != src)
			{
				Iterator <Vertex> Ves = vertices[i].getPrev().iterator();
				while(Ves.hasNext())
				{
					output = new StringBuilder();
					Vertex ves =Ves.next();
					output.append((i+1)+","+(ves.getId()+1));
					while(ves.getPrev()!=null)
					{
						output.append(","+(ves.getPrevious().getId()+1));
						ves = ves.getPrevious();
					}
					output.append("\n");
					results.append(output.toString());
					//System.out.println(output.reverse().toString());
				}
			
			}
			//results.append(output.toString());
			
		}
		System.out.println(results.toString());
		return results;
	}
	void dijkstra(int graph[][], int src,int graph_delay[][])
    {
    	//initialization for width and previous
		List<Vertex> prev = null;
        Vertex vertices [] = new Vertex [V];
        Queue<Integer> Q = new LinkedList<Integer>();
        int width[] = new int[V];
        int i = 0;
        for(i=0;i<V;i++)
        {
        	
        	Vertex temp = new Vertex();
        	temp.setId(i);
        	vertices[i] = temp;
        	vertices[i].setwidth(-Integer.MAX_VALUE);
        	width[i] = -Integer.MAX_VALUE;
        	Q.add(i);
        }
		width[src]=Integer.MAX_VALUE;
		vertices[src].setwidth(Integer.MAX_VALUE);
		while(!Q.isEmpty())
		{
			i = 0;
        	//clone a Queue Q_temp of Q to find the largest width in Q
        	Queue<Integer> Q_temp = new LinkedList<Integer>(Q);
        	int temp_max = -1;
        	int u = -1;
        	while(!Q_temp.isEmpty())
        	{
        		int idx = Q_temp.poll();
        		//System.out.println((idx+1));
        		//find the idx of the largest width in Q
        		if(width[idx]>temp_max)
        		{
        			temp_max = width[idx];
        			u = idx;
        		}
        	}
        	//System.out.println("Removed from Q"+(u+1) + "with width" + width[u]);
        	//remove the largest width from Q
        	Q.remove(u);
        	
        	Queue<Integer> Q_temp2 = new LinkedList<Integer>(Q);
        	int new_array [] = new int [V];
        	
        	while(!Q_temp2.isEmpty())
        	{
        		int temp_array_elem = Q_temp2.poll();
        		new_array[temp_array_elem] = 1;
        	}
        	if(width[u] == -Integer.MAX_VALUE) break;
		
			int v = 0;
			
			for(v=0;v<V;v++)
			{
				//if v is u's neighbor
				if(graph[u][v] != 0 && new_array[v]==1)
				{
					System.out.println("uv is "+ (u+1) +"width is " +width[u]  + "\n nv is " + (v+1) +" width is "+width[v]);
					prev = vertices[v].getPrev();
					int alt = Math.max(width[vertices[v].getId()],Math.min(width[u],graph[u][vertices[v].getId()]));
					if(alt>width[vertices[v].getId()])
					{
						Q.remove(v);
						
						width[vertices[v].getId()] = alt;
						vertices[v].setPrevious(vertices[u]);
						prev = new ArrayList<Vertex>();
						prev.add(vertices[u]);
						vertices[v].setPrev(prev);
						Q.add(v);
						System.out.println("ID" + (v+1) +"'s previous is " + (u+1));
					}
					else if(alt==width[vertices[v].getId()])//&&(graph[u][v]>=width[v]))// || graph[u][v]>=width[u]))
					{
						if (prev != null)
						{
							prev.add(vertices[u]);
						}
						else {
							prev = new ArrayList<Vertex>();
							prev.add(vertices[u]);
							vertices[v].setPrev(prev);
						}
						System.out.println("ID" + (v+1) +"'s previous is " + (u+1));
					}
				}
			}
		}
		int counter = 0;
		for(counter=0;counter<V;counter++)
		{
			//System.out.println((src+1)+" to "+(counter+1) +" max width is " + (width[counter]));
		}
		StringBuilder widest_paths = new StringBuilder();
		widest_paths = Print_func(prev,vertices,src);
		
		Find_shortest(widest_paths,graph_delay);
    }
	void Find_shortest(StringBuilder widest_paths,int [][] graph_delay)
	{
		//System.out.println(widest_paths.toString());
		Hashtable <Integer,Integer> dist = new Hashtable <Integer,Integer>();
		StringBuilder final_result =new StringBuilder();
		String all_paths = widest_paths.toString();
		String paths[] = all_paths.split("\n");
		String temp_string = "";
		int i = 0;
		int end_node = -1;
		int max_for_same = Integer.MAX_VALUE;
		int max_temp = 0;
		while(i<paths.length)
		{
			max_temp = 0;
			max_for_same = 0;
			String nodes [] = paths[i].split(",");
			//update the end_node
			end_node = Integer.parseInt(nodes[0].trim());
			//found the same destination path
			
			if(dist.containsKey(end_node))
			{
				int last_string_ind = final_result.indexOf(temp_string);
				int j = 0;
				while(j<nodes.length-1)
				{
					max_for_same+=graph_delay[Integer.parseInt((nodes[j]).trim())-1][Integer.parseInt(nodes[j+1].trim())-1];
					j++;
				}
				System.out.println(paths[i].toString()+"has the same width, and its delay is " + max_for_same);
				//if shorter path is found, then replace with last path, and update the table
				if(max_for_same < dist.get(end_node))
				{
					dist.put(end_node, max_for_same);
					final_result.replace(last_string_ind, final_result.length()-1, paths[i].toString());
					temp_string = paths[i].toString();
				}
			}
			//this is the only path
			else
			{
				
				int j = 0;
				while(j<nodes.length-1)
				{
					max_temp+=graph_delay[Integer.parseInt((nodes[j]).trim())-1][Integer.parseInt(nodes[j+1].trim())-1];
					j++;
				}
				//update delay table for each destination
				dist.put(end_node, max_temp);
				System.out.println(paths[i].toString() + "has shortest path at delay "+max_temp);
				temp_string = paths[i].toString();
				final_result.append(paths[i]+"\n");
			}
			
			i++;
		}
		System.out.println("Final paths are " + final_result.reverse().toString());

	}
	
	// Driver method
    public static void main (String[] args)
    {
        /* Let us create the example graph discussed above */
       int graph[][] = new int[][]{{0, 80, 0, 200, 0, 80},
                                  {80, 0, 50, 0, 180, 0 },
                                  {0, 50, 0, 80, 0, 150 },
                                  {200, 0, 80, 0, 100, 0 },
                                  {0, 180, 0, 100, 0, 0 },
                                  {80, 0, 150, 0, 0, 0 }
                                 };
       int graph_delay[][] = new int [][]{{0,10,0,30,0,10,},{10,0,10,0,20,0,},{0,10,0,5,0,20,},{30,0,5,0,10,0,},{0,20,0,10,0,0,},{10,0,20,0,0,0,}};
        ShortestPath t = new ShortestPath();
        t.dijkstra(graph, 5,graph_delay);
    }
}
