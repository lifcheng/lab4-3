package lab2;
import java.io.*;
import java.util.regex.*;
public class search {
	public static void main(String args[]) throws IOException
	{
		int i;
		//float REQ[][]=GetREQ();
		for(i=0;i<4;i++)
		{
			char p[]=GetProcess(i);
			SelectService(p);
		}
		
	}
	
	public static char[] GetProcess(int i)throws IOException
	{
		int j=0;
		FileReader pro= new FileReader("PROCESS.txt");
		BufferedReader br = new BufferedReader(pro);
		String line,Process="";
		while(j<=i)
		{
			line=br.readLine();
			Pattern p = Pattern.compile ("[A-Z]");
			Matcher m = p.matcher(line);
			while (m.find())
			{
				Process+=m.group();	
			}
			j++;
		}
		//System.out.println(Process);
		char p[]=Process.toCharArray();
		br.close();
		return p;
	}
	
	public static float[][] GetREQ()throws IOException
	{
		int i=0;
		float r[][]=new float[4][2];
		FileReader req= new FileReader("REQ.txt");
		BufferedReader br = new BufferedReader(req);
		String line=null;
		while((line =br.readLine())!=null)
		{
			String[] REQ= line.split("\\(|,|\\)");
			r[i][0]=Float.parseFloat(REQ[1]);
			r[i][1]=Float.parseFloat(REQ[2]);
			i++;
		}
		br.close();
		return r;
	}
	
	public static void SelectService(char pp[])throws IOException
	{
		
		int n = 0;
		int BestSer[]= new int[14];
		float Q[]={1,0};
		float SERVICE[][]= new float[14*500][3];//服务的可靠性和价格
		String line=null;
		FileReader ser =  new FileReader("SERVICE.txt");
		BufferedReader br2 = new BufferedReader(ser);
		while((line =br2.readLine())!=null)
		{

			String[] Ser= line.split(" ");
			SERVICE[n][0]=Float.parseFloat(Ser[2]);
			SERVICE[n][1]=Float.parseFloat(Ser[4]);
			SERVICE[n][2]=SERVICE[n][0]-SERVICE[n][1]/100;
			n++;
		}
		int i,j;
		n=0;
		char p[]=new char[14];
		for(i=0;i<pp.length;i++)
		{
			for(j=0;j<n;j++)
			{
				if(p[j]==pp[i])
					break;
			}
			if(j==n)
			{
				p[j]=pp[i];
				n++;
			}
		}
		//System.out.println(p);
		for (i=0;i<BestSer.length;i++)
		{
			BestSer[i]=i*500;  //initiation
			for (j=i*500+1;j<(i+1)*500;j++)
			{
				if (SERVICE[j][2]>SERVICE[BestSer[i]][2])
					BestSer[i] = j;
			}
		}
		for(i=0;i<p.length;i++)
		{
			j=(int)(p[i]-'A');
			if (j>=0&&j<=25)
			{
				Q[0]*=SERVICE[BestSer[j]][0];
				Q[1]+=SERVICE[BestSer[j]][1];
			}
			
		}
		//System.out.println("Reliability"+"="+Q[0]);
		//System.out.println("Price"+"="+Q[1]);
		br2.close();
		BufferedWriter bw=new BufferedWriter(new FileWriter("RESULT.txt",true));
		StringBuilder sb = new StringBuilder();
		/*for(i=0;i<p.length;i++)
		{
			sb.append(p[i]+"-"+BestSer[i]%500+"  "+SERVICE[BestSer[i]][0]+" "+SERVICE[BestSer[i]][1]);
			bw.write(sb.toString());
			System.out.println(sb.toString());
			sb.delete(0,sb.length());
			bw.newLine();
		}*/
		for(i=0;i<pp.length;i++)
		{
			if(i%2==0)
				sb.append("(");
			sb.append(pp[i]+"-"+BestSer[(int)(pp[i]-'A')]%500+",");
			if(i%2==1)
			{
				sb.deleteCharAt(sb.length()-1);
				sb.append("),");
				if(i==pp.length-1)
				{
					sb.append("Reliability="+Q[0]+",Price="+Q[1]+",Q="+(Q[0]-Q[1]/100));
				}
				bw.write(sb.toString());
				sb.delete(0,sb.length());
			}
		}
		bw.newLine();
		bw.newLine();
		bw.close();
		}
}
