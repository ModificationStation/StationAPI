# -*- coding: utf-8 -*-
"""
Created on Fri Apr  8 12:50:02 2011

@author: ProfMobius
@version : v0.1
"""

def readsrgs(filename):
    """Reads a Searge RG script and returns a dictionnary of lists for packages, classes, methods and fields"""

    packages=[]; classes=[]; methods=[]; fields=[]
    srgsdata = open(filename,'rb').read().splitlines()

    for row in srgsdata:
        row = row.strip()
        #HINT: We check for comments and whitelines
        if not row or row[0] == '#':continue

        #HINT: We extract the flag for the row (first element of a split on ':')
        if not len(row.split(':')) == 2: raise Exception("The row is not conforming to TAG:ENTRY syntax. [%s]"%row)
        flag = row.split(':')[0]
        row  = row.split(':')[1].strip()
        if not flag in ['PK', 'CL', 'FD', 'MD']:raise Exception("Flag not recognized : %s"%flag)

        #HINT: We check the value of the flag, and append the corresponding list
        #The way we are splitting, the file does support comments after the useful text
        if flag == 'PK': packages.append([row.split()[0], row.split()[1]])
        if flag == 'CL':  classes.append([row.split()[0], row.split()[1]])
        if flag == 'FD':   fields.append([row.split()[0], row.split()[1]])
        if flag == 'MD':  methods.append([' '.join(row.split()[0:2]), ' '.join(row.split()[2:4])])

    return {'PK':packages, 'CL':classes, 'FD':fields, 'MD':methods}

def writesrgs(filename, data):
    """Writes a srgs file based on data. Data is formatted similar to the output of readsrgs (dict of lists)"""
    if not 'PK' in data or not 'CL' in data or not 'FD' in data or not 'MD' in data:
        raise Exception("Malformed data for writesrgs. Keys should be in ['PK', 'CL', 'FD', 'MD']")

    srgsout = open(filename,'w')

    #HINT: We write all the entries for a given key in order. The line ending is set to \r\n for windows compatibility
    for key in ['PK', 'CL', 'FD', 'MD']:
        for entry in data[key]:
            srgsout.write('%s: %s %s\r\n'%(key, entry[0], entry[1]))

    srgsout.close()
