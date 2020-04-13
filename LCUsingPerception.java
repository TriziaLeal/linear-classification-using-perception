import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;


public class LCUsingPerception{
	double r;
	double t;
	double b;
	ArrayList<TrainingData> data; 
	ArrayList<Double> x0s;
	ArrayList<Double> x1s;
	ArrayList<Double> zs;
	public LCUsingPerception(){
		data = new ArrayList<TrainingData>();
		x0s = new ArrayList<Double>();
		x1s = new ArrayList<Double>();
		zs = new ArrayList<Double>();
	}

	void readFile(){
		String line;
		try{	
			File file = new File("input.txt");
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			this.r = Double.parseDouble(br.readLine());
			this.t = Double.parseDouble(br.readLine());
			this.b = Double.parseDouble(br.readLine());
			System.out.println(this.r);
			System.out.println(this.t);
			System.out.println(this.b);

			String[] sub;
			while ((line = br.readLine()) != null){
				sub = line.split(" ");
				x0s.add(Double.parseDouble(sub[0]));
				x1s.add(Double.parseDouble(sub[1]));
				zs.add(Double.parseDouble(sub[2]));
			}


		}catch(Exception e){
			e.getMessage();
		}	

	}

	void writeFile(){
		String line = "x0\t\t\tx1\t\t\tb\t\t\tw0\t\t\tw1\t\t\twb\t\t\ta\t\t\ty\t\t\tz\t\t\t\n";
		for (TrainingData d: this.data){
			line += (String.format("%.2f",d.x0) + "\t\t" +String.format("%.2f",d.x1) +"\t\t" +String.format("%.2f",this.b) +"\t\t" +String.format("%.2f",d.w0) + "\t\t" +String.format("%.2f",d.w1) + "\t\t" +String.format("%.2f",d.wb) + "\t\t" +String.format("%.2f",d.a) + "\t\t" +String.format("%.2f",d.y) + "\t\t" +String.format("%.2f",d.z) + "\t\t\n");
		}
		line +=String.format("%.1f",this.data.get(32).w0);
		try{    
        	FileWriter fw=new FileWriter("output.txt");    
        	fw.write(line);    
        	fw.close();    
        }catch(Exception e){System.out.println(e);}  

	}

	void LinearClassification(){
		TrainingData temp;
		while(true){
			temp = new TrainingData(this.x0s.get(0),this.x1s.get(0),this.zs.get(0));
			this.x0s.add(this.x0s.remove(0));
			this.x1s.add(this.x1s.remove(0));
			this.zs.add(this.zs.remove(0));
			if (this.data.size()==0){
				this.data.add(temp);
				continue;
			}
			adjustWeight(temp,this.data.get(this.data.size()-1));
			temp.a = computeA(temp);
			temp.y = computeY(temp);
			this.data.add(temp);

			if (this.data.size()%this.x0s.size() == 0){
				if (isConverge(this.data.subList(this.data.size()-4, this.data.size())))
					break;
			}
		}
	}

	boolean isConverge(List<TrainingData> data){
		double temp;
		temp = data.get(0).w0;

		for (TrainingData d: data){
			if (temp != d.w0){
				return false;
			}
		
		}
		temp = data.get(0).w1;
		for (TrainingData d: data){
			if (temp != d.w1)
				return false;
		}
		
		temp = data.get(0).wb;
		for (TrainingData d: data){
			if (temp != d.wb)
				return false;
		}

		return true;
	}

	double computeA(TrainingData d){
		return d.x0*d.w0+d.x1*d.w1+this.b*d.wb;
	}

	double computeY(TrainingData d){
		if (d.a > this.t)
			return 1;
		return 0;
	}

	void adjustWeight(TrainingData datanew,TrainingData datacur){
		datanew.w0 = datacur.w0 + this.r*datacur.x0*(datacur.z-datacur.y);
		datanew.w1 = datacur.w1 + this.r*datacur.x1*(datacur.z-datacur.y);
		datanew.wb = datacur.wb + this.r*this.b*(datacur.z-datacur.y);
	}

	void printTD(ArrayList<TrainingData> data){
		System.out.println("x0\tx1\tb\tw0\tw1\twb\ta\ty\tz\t");
		int c = 0;
		for (TrainingData d: data){
			c++;
			System.out.println(String.format("%.1f",d.x0) + "\t" +String.format("%.1f",d.x1) +"\t" +String.format("%.1f",this.b) +"\t" +String.format("%.1f",d.w0) + "\t" +String.format("%.1f",d.w1) + "\t" +String.format("%.1f",d.wb) + "\t" +String.format("%.1f",d.a) + "\t" +String.format("%.1f",d.y) + "\t" +String.format("%.1f",d.z) + "\t");
			if (c%4 == 0)
				System.out.println();
		}

	}
}