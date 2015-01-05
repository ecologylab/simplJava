//simpl.js
//Author Rhema Linder
//Use the methods simplDeserialze and simplSerialze to de/serialze simpl objects.
//simpl objects can be made by using simplTranslator and annotating java code.
/////////////////////


var _simpl_ref_index_map = {};
var _simpl_class_descriptor_map = {};


var _db_almost_never = 3;
var _db_light_warning = 2;
var _db_warning = 1;
var _db_critical_error = 0;

var _db_level = _db_critical_error;//set to a larger number for more reporting

//look for firebug var so that you can use alert or something
function console_message(input, level)
{
   if (console.log) {
     if(!level)
     {
        console.log(input);
     }
     else
     {
        if(level <= _db_level)
           console.log(input);
     }
   }
   else
   {
      alert("You really really need firebug installed to do debugging.");
   }
}

function addOrGetFromClassDescriptorMap(function_name,descriptor_json)
{
   console_message("addOrGetFromClassDescriptorMap",3);
   var returnObject = null;
   returnObject = _simpl_class_descriptor_map[function_name];
   if(returnObject)
      return returnObject;
   if(function_name == "class_descriptor" || function_name == "field_descriptor")
   {
      returnObject = descriptor_json;
   }
   else
   {
      returnObject = simplDeserialize(descriptor_json);
   }
   _simpl_class_descriptor_map[function_name] = returnObject;
   return returnObject;
}

//This function takes a string that has been deserialized by simpl into json.
function simplDeserialize(json_string)
{
    console_message("simplDeserialize",3);
    console_message("simplDeserializeString:"+json_string,3);
	var total = 0;
	var name = "";
	var uncastObject = json_string;
	if(typeof json_string == "string")
		uncastObject = eval('('+json_string+')');
	
	for(var topLevel in uncastObject)
	{ 
		name = topLevel;
		console_message("name in top level is...",3);
		console_message(name,3);
		total+=1;
	}
	if(total != 1)
	{
		alert("Error. There should only be 1"+"top level name in this object.");
	}
	if(name == "object")
	{
	    //alert(JSON.stringify(json_string));
	    return json_string['object'];
	}
	else if(typeof eval(name) != "function")
	{
		alert("Error. "+name+" is not in your object definitions object.");
	}
	var contructor = (eval(''+name+''));
	return new contructor(uncastObject[name]);
}

function no_underscore_replacer(key, value)
{
  if(key == "simpl.id")
  {
    if(typeof(value) != "string")
    {
    	return undefined;
    }
  }
  if (key[0]=="_")
  {
      return undefined;
  }
  else return value;
}

//Works just like JSON.stringify but ignores any fields with an underscore prefix
function simplStringify(obj)
{
    console_message("simplStringify",3);
    return JSON.stringify(obj, no_underscore_replacer);
}


//This function is needed to support simpl type maps.
function reformatObject(obj, unwrap) {
        console_message("reformatObject",3);
    
        if(unwrap)//Unwrap puts associative arrays back into Arrays.
        {
           var k = Array();
           for(var i in obj)
              k.push(reformatObject(obj[i]));
           return k;
        }
        var clone = {};
        for(var i in obj) {
            if(i[0] == "_")
              continue;
            var type_inside = null;
            if(obj._simpl_map_types)
            {
               type_inside = obj._simpl_map_types[i];
            }
            if(typeof(obj[i])=="object")
            {
                if(type_inside)
                  clone[i] = reformatObject(obj[i],true);
                else
                  clone[i] = reformatObject(obj[i]);
            }
            else
                clone[i] = obj[i];
        }
        return clone;   
    }


//This serialized object.
function simplSerialize(simpl_object)//we could add something for this...
{
    console_message("simplSerialize",3);
    simpl_object = recursivlyGiveEachSimplObjectAnId(simpl_object);
    var simpl_object_2 = recursivlyFillReferences(simpl_object);

    var return_object = new Object();
    return_object[""+(simpl_object_2._simpl_object_name)] = simpl_object_2;
    return_object = reformatObject(return_object);
    simpl_object = cleanAsYouGo(simpl_object);
	return simplStringify(return_object);
}


//This function makes maps and collections.
function castCollectionOrMap(ownerObject,deserializedCollection,item_key)
{
  console_message("castCollectionOrMap",3);
  var top_name = null;
  for(var sub in deserializedCollection)
    top_name = sub;
  var innerItems = deserializedCollection[sub];
  var collection = Array();
  var map = {};
  var innerItemType = ownerObject._simpl_collection_types[top_name];
  var is_map = false;
  var is_composite = false;
  if(!innerItemType)
  {
    is_map = true;
    innerItemType = ownerObject._simpl_map_types[top_name];
    console_message(top_name,3);
    if(!innerItemType)
    {//gb probably broked...
       console_message("ownerobject is",3);
       console_message(ownerObject,3);
       console_message("top object don't break is",3);
       console_message(top_name,3);
       console_message(item_key,3);
       var compositeType = ownerObject._simpl_composite_types[top_name];
       if(compositeType)
       {
          console_message("congrats on finding this composite object",3);
          is_composite = true;
       }
       else if(item_key == "object")
       {
          console_message("item_key is an object",3);
       }
       else if(ownerObject[top_name]['simpl.ref'])
       {
         alert("This is a reference object.");//this probably does not go here...
        }
        else
       {
  	       alert("This should be a map or a collection but it isn't.");
       }
      
    }
  }
  for(var sub in innerItems)
  {
    var item = {};
    if(innerItemType)//for collections of composites
        item[innerItemType] = innerItems[sub];
    else
        item = innerItems[sub];
    {
      if(is_composite)
      {
           console_message("just a composite so I did nothing with",3);
           console_message(item,3);
      }
      else
      {
        var item =  simplDeserialize(item);
        if(is_map)//need to check for object type... for which we have a special case of associative arrays that have keys that are the same as their value
        {
          if(is_map)
          {
             console_message("Found something to put in this map.. from the item",3);
              console_message(item_key,3)
             //console_message(item,3);//need to replace stuff here
             ownerObject[''];
             //map[item[item._map_key]] = item;
             map[item[item_key]] = item;
          }
        }
        else
          collection.push(item);
        }
     }
  }
  if(is_map)
    return map;
  else
    return collection;
}

//json constuct works as a constructor for simpl objects from json.  This is called from simplDeserialize
function jsonConstruct(json,target_object)
{
     console_message("jsonConstruct",3);
     target_object['_simpl_class_descriptor'] = addOrGetFromClassDescriptorMap(target_object['_simpl_object_name'],target_object['_simpl_class_descriptor']);

     
     for(field in json)
     {
        console_message("construct field is:"+field,3);
        if(typeof json[field] != "object")
        {
            if(field == "simpl.id")
            {
               console_message("simpl.id is",3);
               console_message(json[field],3);
               _simpl_ref_index_map[json[field]] = target_object;
               target_object["_simpl_id"] = json[field];
            }
            else
            {
              target_object[field] = json[field];
            }
        }
        else
        {
            var collection = {};
            collection[field] = json[field];
            if(collection[field]['simpl.ref'])
            {
               console_message("wait, we should do something different with this..",3);
               console_message(collection[field]['simpl.ref'],3);
               console_message("and I hope that was the number",3);
               //null may be needed here...
               target_object[field] = _simpl_ref_index_map[collection[field]['simpl.ref']];
            }
            else
            {
            console_message("field of the collection or map",3);
            console_message(field,3);
            var item_key = target_object['_simpl_map_types_keys'][field];
            target_object[field] = castCollectionOrMap(target_object,collection,item_key);
            }
        }
     }
     //check on type of class descriptor
     //target_object['_simpl_class_descriptor'] = simplDeserialize(target_object['_simpl_class_descriptor']);
}


///
///
///
//graph part---------------
//graphing happens with 2 passes of recursive functions
//
//1. pass, labels everything with a simpl_id
//2. pass, 
//
//

function getNewUnusedId()
{
  var same = 1;
  var returnId = 1;
  while(same)
  {
    returnId = ""+Math.floor(Math.random()*100000000);
    if(!(returnId in _simpl_ref_index_map))
      same = 0;
  }
  return returnId;
}

function isSimplObject(obj)
{
   if(!obj)
     return false;
   if(typeof(obj) == "object" && '_simpl_object_name' in obj)
      return true;
    return false;
}

function cleanAsYouGo(obj, seen_before_things)
{
   if(!seen_before_things)
       seen_before_things = {};

   if(isSimplObject(obj))
   {
      if(obj['simpl.id'])
      {
        if(obj['simpl.id'] in seen_before_things)
        {
           return obj;
        }
        seen_before_things[obj['simpl.id']] = 1;
      }
   }
    for(var i in obj) {
    //should only be for simpl objects inside of simpl objects but what about collections>>>???
       if(isSimplObject(obj[i]))
           cleanAsYouGo(obj[i],seen_before_things);
    }
    
   if(isSimplObject(obj))
   { 
      obj['simpl.id'] = null;
      obj._seen_before = null;
   }
   return obj;
}

function cleanupGraphContex(obj)
{
    console_message("cleanupGraphContex",3);
    if(isSimplObject(obj))
    {
       console_message("this is a simpl object",3);
      if('_simpl_crumbs_cleanup' in obj)
      {
       console_message("simpl crumbs do exist",3);
        for(var i=0; i<obj._simpl_crumbs_cleanup.length; i+=1) {
          console_message("cleaning stuff up",3);
          obj._simpl_crumbs_cleanup[i]['simpl.id'] = null;
          obj._simpl_crumbs_cleanup[i]._seen_before = undefined;
        }
      }
    }
    //return obj;
}


//this pass adds id's to objects... i may want to start with a clone but i'm not sure
function recursivlyGiveEachSimplObjectAnId(obj, cleanup_crumb_holder)
{
    console_message("recursivlyGiveEacbSimplObjectAnId:"+typeof(obj)+":"+obj,3);
    var crumb = null;
    if(cleanup_crumb_holder)
       crumb = cleanup_crumb_holder;
    else
       crumb = obj;
    if(!('_simpl_crumbs_cleanup' in crumb))
       crumb._simpl_crumbs_cleanup = new Array();
       

    if(typeof(obj) == "object" && '_simpl_object_name' in obj)
    {
      crumb._simpl_crumbs_cleanup.push(obj);
      console_message("Need to add an id here",3);
      if(obj['_seen_before'])
      {
         obj['simpl.id'] = getNewUnusedId();
         return;
      }
      obj['_seen_before'] = 1;
    }
    else
    {
      console_message("this is not a simpl object");
    }
    for(var i in obj) {
    //should only be for simpl objects inside of simpl objects but what about collections>>>???
       if(isSimplObject(obj[i]))
           recursivlyGiveEachSimplObjectAnId(obj[i],crumb);
    }
    return obj;
}

function recursivlyFillReferences(obj,old_clone)
{
   console_message("recursivlyFillReferences",3);
   var clone = {};
   if(old_clone)
      clone = old_clone;
   for(var i in obj) 
   {
      //if(typeof(obj[i]) == "object")
      //{
      //   clone[i] = obj[i];
     //    console_message(i+" was just an object that should not be done anything with...",3);
         
      //}
      //else 
      if(isSimplObject(obj[i]))//'_simpl_object_name' in obj[i])
      {
         console_message("simpl object found",3);
         if('simpl.id' in obj[i])
         {
            console_message("simpl.id found in object",3);
            if(obj[i]['simpl.id'] in _simpl_ref_index_map)
            {
                console_message("This object is already made, referencing it",3);
                refedObj = {};
                refedNumber = {};
                refedNumber['simpl.ref'] = obj[i]['simpl.id'];
                refedObj[obj['_simpl_object_name']] = refedNumber;//old bad
                ////refedObj[i] = refedNumber;
                clone[i] = refedNumber;//refedObj;
                //refedNumber;//no really old
            }
            else
            {
               //add to _simpl_ref_index_map
               //clone
               console_message("This object has not been made.  Making it.",3);
               _simpl_ref_index_map[obj[i]['simpl.id']] = obj[i];
               clone[i] = obj[i];
               recursivlyFillReferences(obj[i],clone);
            }
          }
          else
          {
            console_message("Cloneing field " + i + " with teh val "+obj[i],3);
            clone[i] = obj[i];
          }

       }
       else
       {
         console_message("Cloneing field " + i + " with teh val "+obj[i],3);
         clone[i] = obj[i];//could probably make a better structure for this starement
       }
    }
    return clone;
}