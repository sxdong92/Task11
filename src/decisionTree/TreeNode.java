package decisionTree;
 
import java.util.ArrayList;  
import java.util.List;


public class TreeNode {  
    public String                  paraName;     
    public List<String>            intervalsList;     
    public List<TreeNode>          childrenList;    
    public List<List<String>>      data;    
    public List<String>            restAttributesList;  
    
    public TreeNode() {  
        this.paraName           = "";  
        this.intervalsList      = new ArrayList<String>();  
        this.childrenList       = new ArrayList<TreeNode>();  
        this.data               = new ArrayList<List<String>>();  
        this.restAttributesList = new ArrayList<String>();  
    }  
    
    public String                  getParaName()              {  return paraName;            }  
    public List<String>            getIntervalsList()         {  return intervalsList;       }  
    public List<TreeNode>          getChildrenList()          {  return childrenList;        } 
    public List<List<String>>      getData()                  {  return data;                }  
    public List<String>            getRestAttributesList()    {  return restAttributesList;  }  
    
    public void          setParaName(String paraName)                           {  this.paraName = paraName;                     }  
    public void          setIntervalsList(List<String> intervalsList)           {  this.intervalsList = intervalsList;           } 
    public void          setChildrenList(List<TreeNode> childrenList)           {  this.childrenList = childrenList;             }  
    public void          setData(List<List<String>> data)                       {  this.data = data;                             }  
    public void          setRestAttributesList(List<String> restAttributesList) {  this.restAttributesList = restAttributesList; }  
}  

