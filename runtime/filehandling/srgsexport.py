# -*- coding: utf-8 -*-
"""
Created on Fri Apr  8 13:44:42 2011

@author: ProfMobius
@version: 0.1
"""

import csv, sys
if sys.version_info[0] == 2:
    import srgshandler
if sys.version_info[0] == 3:
    from . import srgshandler


def writesrgsfromcsvs(csvclass, csvmethods, csvfields, outsrgs, side):
    """Reads 3 CSVs and output a srgs"""

    packages=[]; classes=[]; methods=[]; fields=[]

    #HINT: We are adding the package conversions
    packages.append(['.', 'net/minecraft/src'])
    packages.append(['net', 'net'])
    packages.append(['net/minecraft', 'net/minecraft'])
    if side == 0:
        packages.append(['net/minecraft/client', 'net/minecraft/client'])
        packages.append(['net/minecraft/isom', 'net/minecraft/isom'])
    elif side == 1:
        packages.append(['net/minecraft/server', 'net/minecraft/server'])
    else:
        raise Exception("Side not recognized : %d"%side)


    #HINT: We append the class elements. We also handle the special case of Minecraft, MinecraftApplet, MinecraftServer
    csvreader = csv.DictReader(open(csvclass, 'r'), delimiter=',',quotechar='"', quoting=csv.QUOTE_ALL)
    for row in csvreader:
        if int(row['side']) == side:
            #HINT : Those checks are here to append the proper packages to notch version of the Minecraft, etc.
            #They are needed since we don't have notch package information (lost during recompilation)
            #The skip on start is there because of a quirk of the bot updating process
            #We use recompiled sources, so the bot catches the Start.class which have been added by Searge.
            if row['notch'] in ['Minecraft','MinecraftApplet']:
                row['notch'] = 'net/minecraft/client/%s'%row['notch']
            if row['notch'] in ['MinecraftServer']:
                row['notch'] = 'net/minecraft/server/%s'%row['notch']
            if row['notch'] == 'Start':continue
            classes.append([row['notch'], '%s/%s'%(row['package'],row['name'])])

    #HINT: We append the method elements
    csvreader = csv.DictReader(open(csvmethods, 'r'), delimiter=',',quotechar='"', quoting=csv.QUOTE_ALL)
    for row in csvreader:
        if int(row['side']) == side:
            if row['classnotch'] in ['Minecraft','MinecraftApplet']:
                row['classnotch'] = 'net/minecraft/client/%s'%row['classnotch']
            if row['classnotch'] in ['MinecraftServer']:
                row['classnotch'] = 'net/minecraft/server/%s'%row['classnotch']
            if row['classnotch'] == 'Start':continue
            methods.append(['%s/%s %s'%(row['classnotch'],row['notch'], row['notchsig']),
                            '%s/%s/%s %s'%(row['package'], row['classname'], row['searge'], row['sig'])])

    #HINT: We append the field elements
    csvreader = csv.DictReader(open(csvfields, 'r'), delimiter=',',quotechar='"', quoting=csv.QUOTE_ALL)
    for row in csvreader:
        if int(row['side']) == side:
            if row['classnotch'] in ['Minecraft','MinecraftApplet']:
                row['classnotch'] = 'net/minecraft/client/%s'%row['classnotch']
            if row['classnotch'] in ['MinecraftServer']:
                row['classnotch'] = 'net/minecraft/server/%s'%row['classnotch']
            if row['classnotch'] == 'Start':continue
            fields.append(['%s/%s'%(row['classnotch'],row['notch']),
                           '%s/%s/%s'%(row['package'], row['classname'], row['searge'])])

    srgshandler.writesrgs(outsrgs, {'PK':packages, 'CL':classes, 'FD':fields, 'MD':methods})
