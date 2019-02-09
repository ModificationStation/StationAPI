# -*- coding: utf-8 -*-
"""
Created on Fri May 22 23:32:36 2011

@author: Searge
@version: v1.0
"""
import sys, time, os
from optparse import OptionParser
from commands import Commands
import recompile

def main(conffile=None):
    commands = Commands(conffile)

    commands.logger.info ('> Recompiling')
    recompile.main(conffile)
    commands.logger.info ('> Generating the md5 (client)')
    commands.gathermd5s(0)
    commands.logger.info ('> Generating the md5 (server)')
    commands.gathermd5s(1)

if __name__ == '__main__':
    parser = OptionParser(version='MCP %s' % Commands.MCPVersion)
    parser.add_option('-c', '--config', dest='config', help='additional configuration file')
    (options, args) = parser.parse_args()
    main(options.config)
